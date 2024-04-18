import pandas as pd
import json

def get_people(signgu_cd_nm):
    # CSV 파일에서 데이터를 읽어옴
    #df = pd.read_csv('views\csvFolder\Seoul_people_moving_gu.csv', encoding='cp949')
    if signgu_cd_nm.endswith('동'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_people_moving_dong.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\Seoul_people_moving_dong.csv', encoding='cp949')
        filter_col = 'ADSTRD_CD_NM'
    elif signgu_cd_nm.endswith('구'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_people_moving_gu.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\Seoul_people_moving_gu.csv', encoding='cp949')
        filter_col = 'SIGNGU_CD_NM'
    else:
        raise ValueError("Invalid value for signgu_cd_nm. It should end with '동' or '구'.")
    
    filtered_df = df[(df[filter_col] == signgu_cd_nm) & (df['STDR_YYQU_CD'] == 20233)]

    # 총 유동인구수 가져오기
    total_flpop = filtered_df['TOT_FLPOP_CO'].sum()

# 주중과 주말 유동인구를 합산
    weekday_flpop = filtered_df['MON_FLPOP_CO'].sum() + filtered_df['TUES_FLPOP_CO'].sum() + filtered_df['WED_FLPOP_CO'].sum() + filtered_df['THUR_FLPOP_CO'].sum() + filtered_df['FRI_FLPOP_CO'].sum()
    weekend_flpop = filtered_df['SAT_FLPOP_CO'].sum() + filtered_df['SUN_FLPOP_CO'].sum()

    # 주중과 주말 유동인구 비율 계산
    weekday_ratio = round((weekday_flpop / total_flpop) * 100, 1)
    weekend_ratio = round((weekend_flpop / total_flpop) * 100, 1)

    # 각 요일의 유동인구 비율 계산 후 퍼센트로 변환
    mon_ratio = round((filtered_df['MON_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    tues_ratio = round((filtered_df['TUES_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    wed_ratio = round((filtered_df['WED_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    thur_ratio = round((filtered_df['THUR_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    fri_ratio = round((filtered_df['FRI_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    sat_ratio = round((filtered_df['SAT_FLPOP_CO'].sum() / total_flpop) * 100, 1)
    sun_ratio = round((filtered_df['SUN_FLPOP_CO'].sum() / total_flpop) * 100, 1)

    # 결과를 JSON 형식으로 반환
    result = {
        '전체유동인구': int(total_flpop),
        '주중': weekday_ratio,
        '주말': weekend_ratio,
        '월요일': mon_ratio,
        '화요일': tues_ratio,
        '수요일': wed_ratio,
        '목요일': thur_ratio,
        '금요일': fri_ratio,
        '토요일': sat_ratio,
        '일요일': sun_ratio
    }
 # 정렬된 튜플 리스트를 다시 딕셔너리로 변환
    result = dict(result)
    #return json.dumps(result, indent=4, ensure_ascii=False)
    return result
# 함수 호출
result_json = get_people('상암동')
print(result_json)