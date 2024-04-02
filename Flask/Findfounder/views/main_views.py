# projects/myproject/test/views/main_views.py

from flask import Blueprint, Flask, request, render_template
import boto3, json
from werkzeug.utils import secure_filename
from socket import *
bp = Blueprint('main', __name__, url_prefix='/')


# @bp.route('/')
# def hello():
#         return f'Hello, {__name__}

@bp.route('/receive_string', methods=['POST'])
def receive_string():
        
        #spring 으로부터 json객체를 전달받응
        dto_json=request.get_json()
        #dto_json을 dumps메서드를 사용하여 response에 저장
        response=json.dumps(dto_json,ensure_ascii=False)

        
   	    #Spring에서 받은 데이터를 출력해서 확인
        print(dto_json)
        print(type(dto_json))
        #Spring으로 response 전달
        return response

# 0.0.0.0 으로 모든 IP에 대한 연결을 허용해놓고 포트는 8082로 설정
if __name__ == '__main__':
    bp.run('0.0.0.0', debug=True)