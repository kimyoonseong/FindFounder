import pandas as pd

# CSV 파일에서 데이터를 읽어옴
def get_similar(signgu_cd_nm, svc_induty_cd_nm):
    if signgu_cd_nm.endswith('동'):
    
        #df = pd.read_csv(r'C:/Users/82104/git/FindFounder/Flask/Findfounder/views/csvFolder/Seoul_around_store_dong.csv', encoding='cp949')
        df = pd.read_csv('views/csvFolder/Seoul_around_store_dong.csv', encoding='cp949')
        filter_col = 'ADSTRD_CD_NM'
    elif signgu_cd_nm.endswith('구'):
        
        #df = pd.read_csv(r'C:/Users/82104/git/FindFounder/Flask/Findfounder/views/csvFolder/Seoul_around_store_gu.csv', encoding='cp949')
        df = pd.read_csv('views/csvFolder/Seoul_around_store_gu.csv', encoding='cp949')
        filter_col = 'SIGNGU_CD_NM'
    else:
        raise ValueError("Invalid value for signgu_cd_nm. It should end with '동' or '구'.")
    filtered_df = df[df[filter_col] == signgu_cd_nm]

    filtered_df2 = filtered_df[filtered_df['SVC_INDUTY_CD_NM']==svc_induty_cd_nm][['STDR_YYQU_CD', 'SIMILR_INDUTY_STOR_CO']]
    filtered_df3 = filtered_df2.loc[filtered_df2['STDR_YYQU_CD']>20230]
    print(f"filtered_df : {filtered_df.shape}, filtered_df : {filtered_df2.shape}, filtered_df : {filtered_df3.shape}")
    #filtered_df3.sort_values('STDR_YYQU_CD', inplace = True)
    filtered_df3 = filtered_df3.sort_values('STDR_YYQU_CD')
    # 자치구와 유사업종 점포 수 정보를 담을 딕셔너리 초기화
    statistics = {}

    # 필터링된 데이터에서 유사업종 점포 수 정보 설정
    for index, row in filtered_df3.iterrows():
        stdr_yyqu_cd = int(row['STDR_YYQU_CD'])
        similar_induty_stor_co = int(row['SIMILR_INDUTY_STOR_CO'])
       # print(f"{stdr_yyqu_cd}, {similar_induty_stor_co}")
   # 해당 분기에 해당하는 유사업종 점포 수 정보를 딕셔너리에 추가
        statistics[stdr_yyqu_cd] = similar_induty_stor_co

    # 결과를 JSON 형식으로 반환
    return statistics

# 테스트
# signgu_cd_nm = '연남동'
# svc_induty_cd = 'CS100004'
# svc_induty_cd_nm='양식음식점'
# statistics2 = get_similar(signgu_cd_nm, svc_induty_cd_nm)
# print(statistics2)