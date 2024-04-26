from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
from pycaret.regression import *
from openpyxl import load_workbook
import numpy as np
from sklearn.linear_model import LinearRegression

# 매출 예측
def predict_sales_gu(data,prefer_industry):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    model = load_model(f'views\종욱모델링\{data}')

    result = pd.read_csv(f'views\종욱예측결과\{data}_var_pred_cp949.csv', encoding='cp949') 
    result.drop("prediction_label",axis=1,inplace=True)

    

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
    industry= pd.read_csv('views\csvFolder\IndustryList.csv')
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

# def recommend_top_industries(predictions_dict):
#     # 분기별 상승률이 높은 업종을 저장할 딕셔너리 초기화
#     quarterly_growth = {}

#     # 분기별로 업종별 상승률 계산
#     for industry, quarter_data in predictions_dict.items():
#         # 첫 분기와 마지막 분기의 매출을 이용하여 상승률 계산
#         start_sales = quarter_data[min(quarter_data)]
#         end_sales = quarter_data[max(quarter_data)]
#         growth_rate = (end_sales - start_sales) / start_sales * 100

#         # 업종별 상승률을 딕셔너리에 추가
#         quarterly_growth[industry] = growth_rate

#     # 상승률이 높은 순으로 업종 정렬
#     sorted_industries = sorted(quarterly_growth.items(), key=lambda x: x[1], reverse=True)

#     # 추천 딕셔너리 생성
#     recommendation = {}
#     for i, (industry, growth_rate) in enumerate(sorted_industries, 1):
#         recommendation[i] = industry

#     return recommendation

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