import os
import glob
import json
import pymysql
import numpy as np
import pandas as pd
import joblib
from collections import Counter
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler
from sklearn.metrics import mean_absolute_percentage_error
from tensorflow.keras.models import Sequential, Model
from tensorflow.keras.layers import LSTM, Dense, Dropout, Input
from pycaret.regression import *
from statsmodels.tsa.statespace.sarimax import SARIMAX

df = pd.read_csv('views\csvFolder\Seoul_dong_expand.csv', encoding='cp949')
pd.options.display.float_format = '{:.0f}'.format
df = df.sort_values(by=['STDR_YYQU_CD', 'ADSTRD_CD'], ascending=[True, True])
pd.set_option('display.max_rows',None)

def convert_to_start_date(year_quarter):
    year = int(str(year_quarter)[:4])
    quarter = int(str(year_quarter)[-1])

    if quarter == 1:
        return f"{year}-01-01"
    elif quarter == 2:
        return f"{year}-04-01"
    elif quarter == 3:
        return f"{year}-07-01"
    elif quarter == 4:
        return f"{year}-10-01"
    else:
        return "Invalid quarter"
    
# 데이터프레임에 함수 적용
df['STDR_YYQU_CD'] = df['STDR_YYQU_CD'].apply(convert_to_start_date)

arima = pd.DataFrame(df[['ADSTRD_CD', 'ADSTRD_CD_NM', 'EXPNDTR_TOTAMT']])

arima.index=df['STDR_YYQU_CD']
arima.reset_index(inplace=True)

# 데이터 불러오기
data = arima

def predict_expand_dong(prefer_loc_value):
    # 선택한 자치구의 데이터 불러오기
    #selected_district = 11110515  # ADSTRD_CD 값으로 변경
    
    data = arima[arima['ADSTRD_CD_NM'] == prefer_loc_value]  # 'ADSTRD_CD'로 변경

    # 선택한 자치구의 ADSTRD_CD_NM 가져오기
    #selected_district_name = data['ADSTRD_CD_NM'].iloc[0]  # 첫 번째 행의 'ADSTRD_CD_NM' 값 가져오기
        
    # 데이터 전처리
    data['STDR_YYQU_CD'] = pd.to_datetime(data['STDR_YYQU_CD'])
    data.set_index('STDR_YYQU_CD', inplace=True)

    # SARIMA 모델 훈련
    model = SARIMAX(data['EXPNDTR_TOTAMT'], order=(1, 1, 1), seasonal_order=(1, 1, 1, 12))
    results = model.fit()

    # 2023-07-01부터 2024-10-01까지의 예측
    forecast_start = '2023-07-01'
    forecast_end = '2024-10-01'
    forecast = results.get_prediction(start=pd.to_datetime(forecast_start), end=pd.to_datetime(forecast_end), dynamic=False)
    forecast_values = forecast.predicted_mean
    temp_df = forecast_values.to_frame().reset_index().rename(columns = {'index' : "STDR_YYQU_CD", 'predicted_mean' : 'EXPNDTR_TOTAMT'}, inplace = True)
    
    
    result_df = pd.read_csv('views\csvFolder\Seoul_dong_expand.csv', encoding='cp949')[["STDR_YYQU_CD", 'EXPNDTR_TOTAMT', 'ADSTRD_CD_NM']]
    result_df2 = pd.concat([result_df.loc[result_df['ADSTRD_CD_NM'] == prefer_loc_value], temp_df])[["STDR_YYQU_CD", 'EXPNDTR_TOTAMT']]
    
    prediction_dict = {}
    for index, row in result_df2.iterrows() :
        prediction_dict[row['STDR_YYQU_CD']] = row['EXPNDTR_TOTAMT']
    # print(f"{type(forecast_values)}")
    # print(f)
    # print(result_df2['STDR_YYQU_CD'].to_list())
    # print(type(result_df2['EXPNDTR_TOTAMT']))
    # temp_re = result_df2['EXPNDTR_TOTAMT'].rename(index = result_df2['STDR_YYQU_CD'].to_list())
    # print(temp_re)


    # prediction_dict = temp_dict.to_dict()
    # prediction_dict = result_df2.to_dict()

    # 딕셔너리에서 key를 날짜로, value를 예측값으로 하는 딕셔너리로 변환
    formatted_prediction = {str(key).split()[0]: round(value/3, 0) for key, value in prediction_dict.items()}
                                                    #월별로 전환 분기 3으로나눔
    # JSON 형식으로 변환
    #json_prediction = json.dumps(formatted_prediction, ensure_ascii=False)
    #print(json_prediction)
    #print(json_prediction)
    #print(dict(formatted_prediction))
    return dict(formatted_prediction)