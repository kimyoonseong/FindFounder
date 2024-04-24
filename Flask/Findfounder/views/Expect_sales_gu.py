from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
from pycaret.regression import *

# 예측을 수행하는 함수
def predict_sales_gu(data):
    # 입력 데이터를 모델에 전달하여 예측 수행
    # 2023-07-01부터 2024-10-01까지의 예측
    # pickle 파일에서 모델을 로드
    model = load_model(f'views\종욱모델링\{data}')

    result = pd.read_csv(f'views\종욱예측결과\{data}_var_pred_cp949.csv', encoding='cp949') 
    result.drop("prediction_label",axis=1,inplace=True)

    pred = predict_model(model, data = result)
    
    print(pred)
    return 