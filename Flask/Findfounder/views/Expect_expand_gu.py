from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
#구 지출예측

# 예측을 수행하는 함수
def predict_expand_gu(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    with open(f'views/modelFolder/expect_expand_gu/{data}.pkl', 'rb') as f:
        model = pickle.load(f)
    
    forecast_start = '2019-01-01'
    forecast_end = '2024-10-01'
   
    forecast= model.get_prediction(start=pd.to_datetime(forecast_start), end=pd.to_datetime(forecast_end), dynamic=False)
    forecast_values = forecast.predicted_mean
    #print(forecast_values)
    #print(data)
    #prediction = model.predict(data)
    # 예측 결과를 딕셔너리로 변환
    prediction_dict = forecast_values.to_dict()

    # 딕셔너리에서 key를 날짜로, value를 예측값으로 하는 딕셔너리로 변환
    formatted_prediction = {str(key).split()[0]: round(value/3, 0) for key, value in prediction_dict.items()}
                                                    #월별로 전환 분기 3으로나눔
    # JSON 형식으로 변환
    #json_prediction = json.dumps(formatted_prediction, ensure_ascii=False)
    #print(json_prediction)
    #print(json_prediction)
    #return dict(formatted_prediction)

def predict_expand_gu2(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    with open(f'views/modelFolder/expect_expand_gu/{data}.pkl', 'rb') as f:
        model = pickle.load(f)
    
    forecast_start = '2019-01-01'
    forecast_end = '2024-10-01'
   
    forecast= model.get_prediction(start=pd.to_datetime(forecast_start), end=pd.to_datetime(forecast_end), dynamic=False)
    forecast_values = forecast.predicted_mean
    #print(forecast_values)
    #print(data)
    #prediction = model.predict(data)
    # 예측 결과를 딕셔너리로 변환
    prediction_dict = forecast_values.to_dict()

    # 딕셔너리에서 key를 날짜로, value를 예측값으로 하는 딕셔너리로 변환
    formatted_prediction = {str(key).split()[0]: round(value/3, 0) for key, value in prediction_dict.items()}
                                                    #월별로 전환 분기 3으로나눔
    # JSON 형식으로 변환
    #json_prediction = json.dumps(formatted_prediction, ensure_ascii=False)
    #print(json_prediction)
    #print(json_prediction)
    region_data=pd.read_csv('views/csvFolder/seoul.csv', encoding='utf-8')
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