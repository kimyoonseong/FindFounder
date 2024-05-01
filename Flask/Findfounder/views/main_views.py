# projects/myproject/test/views/main_views.py

from flask import Blueprint, Flask, request, render_template, jsonify
import boto3, json
from werkzeug.utils import secure_filename
from socket import *
from views.Stat  import get_statistics
from views.Industry import read_industry_from_csv
from views.SeoulRegion import read_region_from_csv,get_district_from_subdistrict
from views.SeoulMovingPeople import get_people
from views.SeoulUseMoney import get_use_money
from views.SeoulSimilarStore import get_similar
from views.SeoulZipgack import get_zipgack
from views.Expect_expand_gu import predict_expand_gu,predict_expand_gu2
from views.Expect_expand_dong import predict_expand_dong
from views.Expect_sales_gu import predict_sales_gu, get_pred_var_gu
from views.Expect_sales_dong import predict_sales_dong, get_pred_var_dong
from views.Expect_expand_seoul import predict_expand_seoul
import requests
bp = Blueprint('main', __name__)


# @bp.route('/')
# def home():
# 	return "home"


@bp.route('/call_industry',methods=['POST'])
def call_industry():
        
        category = request.get_data(as_text=True)  # 클라이언트로부터 카테고리를 받음
        industry_list = read_industry_from_csv(category)  # CSV 파일에서 해당 카테고리의 업종 리스트를 가져옴
        print(jsonify(industry_list))  # 업종 리스트를 JSON 형태로 응답
        
        return jsonify(industry_list)

@bp.route('/call_region',methods=['POST'])
def call_region():
        seoulgu = request.get_data(as_text=True)  # 클라이언트로부터 카테고리를 받음
        region_list = read_region_from_csv(seoulgu)  # CSV 파일에서 해당 카테고리의 지역 리스트를 가져옴
        print(jsonify(region_list))  # 업종 리스트를 JSON 형태로 응답
        
        return jsonify(region_list)



@bp.route('/receive_string_result',methods=['POST'])
def receive_result_string():
       #spring 으로부터 json객체를 전달받응
        dto_json=request.get_json()
        #dto_json을 dumps메서드를 사용하여 response에 저장
        response=json.dumps(dto_json,ensure_ascii=False)
        prefer_industry=dto_json.get('preferIndustry')
        prefer_loc_value = dto_json.get('preferLoc')
        
        
        if prefer_loc_value.endswith("구"):
                prediction = predict_expand_gu(prefer_loc_value)# 자치구 매달 지출예측
                prediction_sales = predict_sales_gu(prefer_loc_value,prefer_industry)# 매출예측
                pediction_seoul=predict_expand_seoul()
                pre_var_list = get_pred_var_gu(prefer_loc_value)

                if prefer_industry == "상관없음":
                        industry_cnt = 10
                else :
                        industry_cnt = len(read_industry_from_csv(prefer_industry))
                
                
                combined_data = {
                "gu" : prefer_loc_value,
                "loc_expect_expand": prediction,#매월 구 지출액 및 예측(구단위) 0부터 시작함 
                "loc_expect_expand_whole":pediction_seoul, #매월 모든 구 평균 지출액 및 예측(시 단위)
                "industry_expect_expand": prediction_sales, #매출 예측 
                "pred_var_list" : pre_var_list,
                "industry_cnt" : industry_cnt
                #"변수":
                }
               


        elif prefer_loc_value.endswith("동"):
                prediction = predict_expand_dong(prefer_loc_value) #행정동 매달 지출 예측
                region = get_district_from_subdistrict(prefer_loc_value) #행정동의 자치구 가져오기
                prediction_gu = predict_expand_gu2(region) # 자치구의 행정동 평균 매달 지출 예측
                prediction_dong_expand=predict_sales_dong(prefer_loc_value,prefer_industry,region)
                pre_var_list = get_pred_var_dong(prefer_loc_value)

                if prefer_industry == "상관없음":
                        industry_cnt = 10
                else :
                        industry_cnt = len(read_industry_from_csv(prefer_industry))

                combined_data = {
                "dong"  : prefer_loc_value,
                "gu" : region,
                "loc_expect_expand_gu": prediction_gu,#평균 행정동 매달 지출 예측
                "loc_expect_expand_dong": prediction,  #행정동 매달 지출 예측
                "loc_expect_sales_dong" : prediction_dong_expand, #행정동 업종 매출 예측
                "pred_var_list" : pre_var_list,
                "industry_cnt" : industry_cnt
                }
        else:
        # 예외 처리: "구" 또는 "동"으로 끝나지 않는 경우
                prediction = None

        json_prediction = json.dumps(prediction, ensure_ascii=False)
        print("#@#@#@#@#@#")
        print(json.dumps(combined_data))
        #print(industry_life)
        return jsonify(combined_data)



@bp.route('/receive_string', methods=['POST'])
def receive_string():
        
        #spring 으로부터 json객체를 전달받응
        dto_json=request.get_json()
        #dto_json을 dumps메서드를 사용하여 response에 저장
        response=json.dumps(dto_json,ensure_ascii=False)
        prefer_industry=dto_json.get('preferIndustry')
        prefer_loc_value = dto_json.get('preferLoc')
        print(prefer_loc_value)
   	    #Spring에서 받은 데이터를 출력해서 확인
        print(dto_json)
        print(type(dto_json))
        #print(type(response))
        #print(response)
        #json_data = response.json()
        #prefer_loc_value = json_data.get('preferLoc')
        
        #expend_amount=get_expendstatic(prefer_loc_value)
        #운영 영업 개월
        industry_life = get_statistics(prefer_loc_value)
        #유동인구
        seoul_moving_people=get_people(prefer_loc_value)
        #분기 평균 지출
        seoul_use_money=get_use_money(prefer_loc_value)
        #유사점포
        seoul_similar_store= get_similar(prefer_loc_value,prefer_industry)
        #집객시설
        seoul_zipgack=get_zipgack(prefer_loc_value)
        combined_data = {
                "industry_life": industry_life,
                "seoul_moving_people": seoul_moving_people,
                "seoul_use_money": seoul_use_money,
                "seoul_similar_store": seoul_similar_store,
                "seoul_zipgack" : seoul_zipgack
        }

        #return combined_data
        # 합쳐진 데이터를 JSON 형식으로 반환
        return jsonify(combined_data)

# if __name__ == '__main__':
#     bp.run('0.0.0.0', debug=True)