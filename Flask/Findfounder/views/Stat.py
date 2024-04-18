import pandas as pd

# CSV 파일에서 데이터를 읽어옴


def get_statistics(signgu_cd_nm):
    #df = pd.read_csv('views\csvFolder\VwsmSignguIxQq2.csv', encoding='cp949')
    if signgu_cd_nm.endswith('동'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_people_moving_dong.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\Seoul_average_month_dong.csv', encoding='cp949')
        filter_col = 'ADSTRD_CD_NM'
    elif signgu_cd_nm.endswith('구'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_people_moving_gu.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\VwsmSignguIxQq2.csv', encoding='cp949')
        filter_col = 'SIGNGU_CD_NM'
    else:
        raise ValueError("Invalid value for signgu_cd_nm. It should end with '동' or '구'.")
    # 입력된 자치구에 해당하는 데이터 필터링
    filtered_df = df[df[filter_col] == signgu_cd_nm]

    # 자치구와 상권 변화 지수 정보를 담을 딕셔너리 초기화
    statistics = {}

    # 필터링된 데이터에서 상권 변화 지수 정보 설정
    for index, row in filtered_df.iterrows():
        stdr_yyqu_cd = str(row['STDR_YYQU_CD'])
        trdar_chnge_ix_nm = row['OPR_SALE_MT_AVRG']

        # 해당 분기에 해당하는 상권 변화 지수 정보를 딕셔너리에 추가
        if stdr_yyqu_cd not in statistics:
            statistics[stdr_yyqu_cd] = trdar_chnge_ix_nm
                #딕셔너리의 키를 오름차순으로 정렬하여 키-값 쌍의 튜플 리스트 생성
            sorted_items = sorted(statistics.items())

            # 정렬된 튜플 리스트를 다시 딕셔너리로 변환
            sorted_data = dict(sorted_items)           
        else:
            statistics[stdr_yyqu_cd] += ', ' + trdar_chnge_ix_nm
        
#
    # 결과를 JSON 형식으로 반환
    return sorted_data

# 테스트
# signgu_cd_nm = '마포구'
# statistics = get_statistics(signgu_cd_nm)
# print(statistics)