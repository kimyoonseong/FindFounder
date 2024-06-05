# FIND FOUNDER
# 우리 FISA 최종 프로젝트
* TEAM : FIFO

* [TEAM NOTION LINK](https://www.notion.so/d05de897559c443eb6e6cc4cd770588f)

* DATE : 2024-03-25 ~ 2024-05-08

* MEMBERS : 김가영, 김윤성, 김찬기, 박명우, 윤종욱

## 만든이유
고령사회에 정년퇴직 이후에도 자영업 희망하는 사람들이 많아졌다지만

창업 위치, 업종, 초기자본, 대출 등 고려할 요소가 많았고,

고금리·고물가·고환율에 저조한 매출, 대출 상환 부담 등 자영업자 경영 환경이 악화되면서 지난해 자영업자 10

명 중 한 명은 가게 문을 닫았기에, 망설이는 사람들이 부지기수다.

그래서 이런 자영업을 희망하는 사람들을 위해 개인 맞춤 AI컨설팅과, 통계를 제공하는 서비스를 만들기로 했다.


## 개요

![개발환경](https://github.com/kimyoonseong/FindFounder/assets/37408405/ea4aa16a-9086-49d9-bf16-29ca2528a794)
![image](https://github.com/kimyoonseong/FindFounder/assets/37408405/8d92ff05-9504-462b-a38a-ebf5bd2ea3e8)
![image](https://github.com/kimyoonseong/FindFounder/assets/37408405/fd2072c3-daa7-4551-9c70-69bc88e1f95d)

## 주요 기능
⭐ 지역 통계 : 서울 시내 지역별 업종 정보 습득(자치구, 행정동)  

⭐ **AI 컨설팅** : 사용자 맞춤 업종 추천 및 인사이트 제공, 대출 상품 정보 제시  

⭐ 커뮤니티 : 사용자들 간 노하우 및 정보 공유  


## **어려웠던 점:**
1. REST API 구축 후 SWAGGER에서 TEST가 끝나도 Front에 지식이 부족하여, api에서 return 된 json값을 화면에 나타내는데 어려움을 겪음.

2. CI/CD구축시 Jenkins 용량문제 어려움을 겪음 주기적으로 Jenkins 서버 용량을 비워주고, 용량을 늘려 해결

3. FE -> Spring Boot-> Flask 로 이동하고 다시 돌아갈때 , 여러 형이 다른 컬럼들로 인한 서빙의 복잡함과, 다른 팀원의 코드 이해의 어려움. 주석과, Key 활용의 중요성을 깨닫고 리팩토링

4. Flask 모델 서빙시, 여러 dictionary값들을 합쳐 json형식으로 만들어야 했는데, 순서가 spring boot로 넘어오며 섞이는 문제, key값으로 이름을지정하여 한번더 감싸서 해결

5. kakao 맵 api 사용시 자치구별로 나눠서 가시적으로 표시해야했는데, 서울시 자치구 SHP파일을 QSIS앱으로 좌표를 Json으로 추출후 , Frontend에서 적용할때, 좌표 용량으로인해 , 용량을 줄이는데 어려움을 겪음

6. 이메일 인증 시스템 : 보안 프로그램에서 이메일을 보호하고 있어서 충돌때문에 이메일 전송이 안됨. 보안 프로그램을 중지시키고 전송해보니 성공함.

7. jwt 인증 : springboot2 버전과 springboot3 버전에서 jwt를 설정하는 데에 차이가 많이 나서 버전에 맞게 수정하니 해결됨. 
   
## **Try (시도할 내용):**
1. ELK를이용한 사용자 로그 수집 및 Grafana , Prometheus를 이용한 시스템 로그수집(완료)
2. 배포환경에서도 동작하여 여러 사용자가 접속했을때 로그 확인
3. 단기, 중기, 장기 기간별 모델을 나누어서 개발하여 서비스 제공
