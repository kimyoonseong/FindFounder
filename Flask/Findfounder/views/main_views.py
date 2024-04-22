# projects/myproject/test/views/main_views.py

from flask import Blueprint, Flask, request, render_template, jsonify
import boto3, json
from werkzeug.utils import secure_filename
from socket import *
from Findfounder.views.Stat  import get_statistics
from Findfounder.views.Industry import read_industry_from_csv
from Findfounder.views.SeoulRegion import read_region_from_csv
from Findfounder.views.SeoulMovingPeople import get_people
from Findfounder.views.SeoulUseMoney import get_use_money
from Findfounder.views.SeoulSimilarStore import get_similar
from Findfounder.views.SeoulZipgack import get_zipgack
import requests
bp = Blueprint('main', __name__, url_prefix='/')


@bp.route('/')
def home():
	return "home"
@bp.route('/call_industry',methods=['POST'])
def call_industry():
        category = request.get_data(as_text=True)  # 클라이언트로부터 카테고리를 받음
        industry_list = read_industry_from_csv(category)  # CSV 파일에서 해당 카테고리의 업종 리스트를 가져옴
        print(jsonify(industry_list))  # 업종 리스트를 JSON 형태로 응답
        
        return jsonify(industry_list)

@bp.route('/call_region',methods=['POST'])
def call_region():
        seoulgu = request.get_data(as_text=True)  # 클라이언트로부터 카테고리를 받음
        region_list = read_region_from_csv(seoulgu)  # CSV 파일에서 해당 카테고리의 업종 리스트를 가져옴
        print(jsonify(region_list))  # 업종 리스트를 JSON 형태로 응답
        
        return jsonify(region_list)


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

if __name__ == '__main__':
    bp.run('0.0.0.0', debug=True)