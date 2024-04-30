from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
from pycaret.regression import *
from openpyxl import load_workbook
import numpy as np
from sklearn.linear_model import LinearRegression
import os


def get_pred_var(data) :

    csv_list = os.listdir(f'./views/pred_dong/pred_var')

    region_csv = [x for x in csv_list if x.endswith(f"{data}_pred_var_15.csv")][0]

    result = pd.read_csv(f'./views/pred_dong/pred_var/{region_csv}', encoding='cp949') 

    pre_var_list = result.columns.to_list()[7:]
    mst_temp = pd.read_excel("./views/csvFolder/automatic_name.xlsx")
    mst_df = mst_temp.drop_duplicates(["A", "B"])
    mst_df = mst_df.loc[(mst_df["A"].notna()) & (mst_df["B"].notna()), ["A", "B"]]
    pre_dict = {}
    for idx, pred_var in enumerate(pre_var_list) :
        pre_dict[idx+1] = mst_df.loc[mst_df["A"] == pred_var, "B"].values[0]

    return pre_dict


# 매출 예측 data =동 region 구
def predict_sales_dong(data,prefer_industry,region):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    # model_list = os.listdir(f"./views/종욱동모델링/{region} - 모델링/")
    model_list = os.listdir(f"./views/pred_dong/model/")
    
    model_pkl = [x for x in model_list if x.endswith(f"{data}.pkl") ][0]
    print(f"model_list : {model_list}, model_pkl : {model_pkl}")
    
    model = load_model(f'./views/pred_dong/model/{model_pkl[:-4]}')


    # result = pd.read_csv(f'views/종욱동예측결과/{region} - SARIMA예측변수/11680510_{data}_pred_var_15.csv', encoding='cp949') 
    # Findfounder/views/종욱동예측결과/강남구 - SARIMA예측변수/11680510_신사동_pred_var_15.csv

    csv_list = os.listdir(f'./views/pred_dong/pred_var')
    
    region_csv = [x for x in csv_list if x.endswith(f"{data}_pred_var_15.csv")][0]
    print(f"region_csv : {region_csv}")
    result = pd.read_csv(f"./views/pred_dong/pred_var/{region_csv}", encoding = "cp949")
    #result.drop("prediction_label",axis=1,inplace=True)
    # Findfounder/views/종욱동예측결과/{region} - SARIMA예측변수/11680510_{data}_pred_var_15.csv
    

    pred = predict_model(model, data = result)
    
   # Excel 파일 열기
    wb = load_workbook(filename='views/csvFolder/automatic_name.xlsx')

    # 첫 번째 시트 선택
    sheet = wb.active

    # 데이터 읽어오기
    data = sheet.values

    columns = next(data)

    # 데이터프레임 생성
    rename_column = pd.DataFrame(data, columns=columns)

    # pred 데이터프레임의 컬럼명을 변경
    pred.rename(columns=dict(zip(rename_column['A'], rename_column['B'])), inplace=True)
    #print(pred)
    industry= pd.read_csv('views/csvFolder/IndustryList.csv')
    #print(industry.columns)
    if prefer_industry in industry.columns:
        category = industry[prefer_industry].dropna().tolist()
        #print(category)
        # pred 데이터프레임에서 해당 업종에 해당하는 행만 남기기
        pred = pred[pred['서비스_업종_코드_명'].isin(category)]
    else:
        pred=pred
    

        # 예측값 딕셔너리 추출
    predictions_dict = extract_predictions(pred)
    predictions_dict2=recommend_top_industries(predictions_dict)
    # 결과 출력
    predictions_dict2_str_keys = {str(k): v for k, v in predictions_dict2.items()}

    # 첫 번째 딕셔너리에 두 번째 딕셔너리를 추가
    predictions_dict.update(predictions_dict2_str_keys)
    #print(pred)
    return predictions_dict


def extract_predictions(pred):
    # 결과를 저장할 딕셔너리 초기화
    predictions_dict = {}
    
    # pred 데이터프레임을 반복하며 정보 추출
    for index, row in pred.iterrows():
        # 서비스 업종 코드명
        service_code = row['서비스_업종_코드_명']
        
        # 기준 년분기 코드
        base_year_quarter = row['기준_년분기_코드']
        
        # 예측값
        prediction = row['prediction_label']
        
        # 딕셔너리에 정보 저장
        if service_code not in predictions_dict:
            predictions_dict[service_code] = {}
        predictions_dict[service_code][base_year_quarter] = prediction
    
    return predictions_dict


def recommend_top_industries(predictions_dict):
    growth_rates = {}

    for industry, quarter_data in predictions_dict.items():
      
        quarters = list(map(int, quarter_data.keys()))  
        sales = list(quarter_data.values())
        quarters = np.array(quarters).reshape(-1, 1)  

        # Perform linear regression
        model = LinearRegression()
        model.fit(quarters, sales)
        slope = model.coef_[0] 
        growth_rates[industry] = slope


    mean_growth = np.mean(list(growth_rates.values()))
    std_growth = np.std(list(growth_rates.values()))
    standardized_growth = {industry: (rate - mean_growth) / std_growth for industry, rate in growth_rates.items()}

    sorted_industries = sorted(standardized_growth.items(), key=lambda x: x[1], reverse=True)

    recommendation = {i: industry for i, (industry, _) in enumerate(sorted_industries, 1)}

    return recommendation