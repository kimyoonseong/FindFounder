from flask import Flask, request, jsonify
import pickle , json
import pandas as pd


# 예측을 수행하는 함수
def predict_expand_gu(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    with open(f'views\modelFolder\expect_expand_gu\{data}.pkl', 'rb') as f:
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
    return dict(formatted_prediction)