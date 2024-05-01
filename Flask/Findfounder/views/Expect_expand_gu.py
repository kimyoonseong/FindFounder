from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
from pycaret.regression import *
from statsmodels.tsa.statespace.sarimax import SARIMAX
#구 지출예측

df = pd.read_csv('./views/csvFolder/Seoul_gu_expand.csv')
pd.options.display.float_format = '{:.0f}'.format
df = df.sort_values(by=['STDR_YYQU_CD', 'SIGNGU_CD'], ascending=[True, True])


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

arima = pd.DataFrame(df[['SIGNGU_CD', 'SIGNGU_CD_NM', 'EXPNDTR_TOTAMT']])

arima.index=df['STDR_YYQU_CD']
arima.reset_index(inplace=True)

# 데이터 불러오기
data = arima

# 예측을 수행하는 함수
def predict_expand_gu(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    # with open(f'./views/modelFolder/expect_expand_gu/{data}.pkl', 'rb') as f:
    #     model = pickle.load(f)
    data1 = arima[arima['SIGNGU_CD_NM'] == data] 
    data1['STDR_YYQU_CD'] = pd.to_datetime(data1['STDR_YYQU_CD'])
    data1.set_index('STDR_YYQU_CD', inplace=True)

    forecast_start = '2019-01-01'
    selected_data1 = data1[data1.index >= forecast_start]
    model = SARIMAX(selected_data1['EXPNDTR_TOTAMT'], order=(1, 1, 1), seasonal_order=(1, 1, 1, 12))
    results = model.fit()
    forecast_end = '2024-10-01'
   
    forecast= results.get_prediction(start=pd.to_datetime(forecast_start), end=pd.to_datetime(forecast_end), dynamic=False)
    forecast_values1 = forecast.predicted_mean

    for index in selected_data1.index:
        if index in forecast_values1.index:
            forecast_values1.loc[index] = selected_data1.loc[index]['EXPNDTR_TOTAMT']
    #print(forecast_values)
    #print(data)
    #prediction = model.predict(data)
    # 예측 결과를 딕셔너리로 변환
    prediction_dict = forecast_values1.to_dict()

    # 딕셔너리에서 key를 날짜로, value를 예측값으로 하는 딕셔너리로 변환
    formatted_prediction = {str(key).split()[0]: round(value/3, 0) for key, value in prediction_dict.items()}
                                                    #월별로 전환 분기 3으로나눔
    # JSON 형식으로 변환
    #json_prediction = json.dumps(formatted_prediction, ensure_ascii=False)
    #print(json_prediction)
    #print(json_prediction)
    return dict(formatted_prediction)

def predict_expand_gu2(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    data1 = arima[arima['SIGNGU_CD_NM'] == data] 
    data1['STDR_YYQU_CD'] = pd.to_datetime(data1['STDR_YYQU_CD'])
    data1.set_index('STDR_YYQU_CD', inplace=True)

    forecast_start = '2019-01-01'
    selected_data1 = data1[data1.index >= forecast_start]
    model = SARIMAX(selected_data1['EXPNDTR_TOTAMT'], order=(1, 1, 1), seasonal_order=(1, 1, 1, 12))
    results = model.fit()
    forecast_end = '2024-10-01'
   
    forecast= results.get_prediction(start=pd.to_datetime(forecast_start), end=pd.to_datetime(forecast_end), dynamic=False)
    forecast_values1 = forecast.predicted_mean

    for index in selected_data1.index:
        if index in forecast_values1.index:
            forecast_values1.loc[index] = selected_data1.loc[index]['EXPNDTR_TOTAMT']
    #print(forecast_values)
    #print(data)
    #prediction = model.predict(data)
    # 예측 결과를 딕셔너리로 변환
    prediction_dict = forecast_values1.to_dict()

    # 딕셔너리에서 key를 날짜로, value를 예측값으로 하는 딕셔너리로 변환
    formatted_prediction = {str(key).split()[0]: round(value/3, 0) for key, value in prediction_dict.items()}
                                                    #월별로 전환 분기 3으로나눔
    # JSON 형식으로 변환
    #json_prediction = json.dumps(formatted_prediction, ensure_ascii=False)
    #print(json_prediction)
    #print(json_prediction)
    region_data=pd.read_csv('./views/csvFolder/seoul.csv', encoding='utf-8')
    # 입력 받은 자치구 값에 대한 고유한 행정동 개수 계산
    result = count_unique_hood(region_data, data)
    print(f"{data}에 해당하는 행정동의 고유 값 개수는 {result}개 입니다.")
    divisor = result
    rounded_result = round_and_divide(formatted_prediction, divisor)
    #print("나눈 결과:", rounded_result)

    #print("이전 formatted_prediction:", formatted_prediction)
    return rounded_result

def count_unique_hood(region_data, gu_value):
    # 주어진 자치구 값에 해당하는 데이터 필터링
    filtered_data = region_data[region_data['자치구'] == gu_value]
    
    # 행정동 컬럼의 고유 값 개수 계산
    unique_hood_count = filtered_data['행정동'].nunique()
    
    return unique_hood_count

def round_and_divide(dictionary, divisor):
    # 딕셔너리의 각 값에 대해 나누고 정수로 반올림
    rounded_dict = {key: round(value / divisor) for key, value in dictionary.items()}
    return rounded_dict