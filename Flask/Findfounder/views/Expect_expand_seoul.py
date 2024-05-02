from flask import Flask, request, jsonify
import pickle , json
import pandas as pd
#서울시 지출예측 (모든 구 다 더해서 /25)


def predict_expand_seoul():

    df = pd.read_csv('./views/csvFolder/Seoul_average_expand.csv', encoding='cp949')
    extracted_data = df[['STDR_YYQU_CD', 'EXPNDTR_TOTAMT_AVG']]

    # 딕셔너리 형식으로 변환하여 반환
    data_dict = extracted_data.set_index('STDR_YYQU_CD').to_dict()['EXPNDTR_TOTAMT_AVG']
    # 'EXPNDTR_TOTAMT_AVG'의 값을 3으로 나누고 딕셔너리 형식으로 변환하여 반환
    extracted_data['EXPNDTR_TOTAMT_AVG'] = round(extracted_data['EXPNDTR_TOTAMT_AVG']/3, 0) #월별로
    data_dict = extracted_data.set_index('STDR_YYQU_CD').to_dict()['EXPNDTR_TOTAMT_AVG']
    return data_dict