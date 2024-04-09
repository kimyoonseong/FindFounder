# projects/myproject/test/views/main_views.py

from flask import Blueprint, Flask, request, render_template, jsonify
import boto3, json
from werkzeug.utils import secure_filename
from socket import *
import requests
bp = Blueprint('main', __name__, url_prefix='/')


@bp.route('/')
def home():
	return "home"


@bp.route('/receive_string', methods=['POST'])
def receive_string():
        
        #spring 으로부터 json객체를 전달받응
        dto_json=request.get_json()
        #dto_json을 dumps메서드를 사용하여 response에 저장
        response=json.dumps(dto_json,ensure_ascii=False)

        
   	    #Spring에서 받은 데이터를 출력해서 확인
        print(dto_json)
        print(type(dto_json))
        print(type(response))
        print(response)
        #Spring으로 response 전달
        return response
# @bp.route('/send_stats', methods=['POST'])
# def send_stats():
#     # 보낼 JSON 데이터 생성
#     stats_data = {
#         "stat1": 100,
#         "stat2": 200,
#         "stat3": 300
#     }

#     # Spring Boot 서버의 엔드포인트 URL
#     url = "http://localhost:8080/stats"

#     # JSON 데이터를 포함한 POST 요청 생성
#     response = requests.post(url, json=stats_data)

#     # 응답 확인
#     if response.status_code == 200:
#         return "Stats sent successfully"
#     else:
#         return "Failed to send stats"



if __name__ == '__main__':
    bp.run('0.0.0.0', debug=True)