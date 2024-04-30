import pandas as pd
def get_use_money(location):
    # 총지출 EXPNDTR_TOTAMT
    if location.endswith('동'):
        #df = pd.read_csv(r'C:/Users/82104/git/FindFounder/Flask/Findfounder/views/csvFolder/Seoul_Use_Earn_Money_dong.csv', encoding='cp949')
        df = pd.read_csv('./views/csvFolder/Seoul_Use_Earn_Money_dong.csv', encoding='cp949')
        filter_col = 'ADSTRD_CD_NM'
    elif location.endswith('구'):
        #df = pd.read_csv(r'C:/Users/82104/git/FindFounder/Flask/Findfounder/views/csvFolder/Seoul_Use_Earn_Money_gu.csv', encoding='cp949')
        df = pd.read_csv('./views/csvFolder/Seoul_Use_Earn_Money_gu.csv', encoding='cp949')
        filter_col = 'SIGNGU_CD_NM'
    else:
        raise ValueError("Invalid value for signgu_cd_nm. It should end with '동' or '구'.")
    #print(location)
    # 2023년 데이터만 추출
    #df_2023 = df[df['STDR_YYQU_CD'].astype(str).str.startswith('2023')]
 
    df_2023 = df[(df['STDR_YYQU_CD'].astype(str).str.startswith('2023')) & (df[filter_col] == location)]
    #print(df_2023)
    # 전체 총 지출 계산
    total_spending = df_2023['EXPNDTR_TOTAMT'].sum()

    #한분기 평균 지출
    sector_spending= int(total_spending/3)

    # 카테고리 컬럼명 수정
    category_columns = {
        'FDSTFFS_EXPNDTR_TOTAMT': '식료품',
        'CLTHS_FTWR_EXPNDTR_TOTAMT': '의류신발',
        'LVSPL_EXPNDTR_TOTAMT': '생활용품',
        'MCP_EXPNDTR_TOTAMT': '의료비',
        'TRNSPORT_EXPNDTR_TOTAMT': '교통비',
        'EDC_EXPNDTR_TOTAMT': '교육비',
        'PLESR_EXPNDTR_TOTAMT': '유흥',
        # 'LSR_CLTUR_EXPNDTR_TOTAMT': '문화생활',
        'ETC_EXPNDTR_TOTAMT': '기타',
        'FD_EXPNDTR_TOTAMT': '식비'
    }

    # 카테고리별 총 지출 합산
    category_spending = df_2023[list(category_columns.keys())].sum()

    # 카테고리별 지출 내림차순 정렬 후 상위 3개 카테고리 추출
    top_categories = category_spending.nlargest(3)

    # 상위 카테고리의 이름 가져오기
    top_category_names = [category_columns[col] for col in top_categories.index]

    return {
        '2023년 분기 평균 지출': sector_spending,
        '1위카테고리': top_category_names[0],
        '2위카테고리': top_category_names[1],
        '3위카테고리': top_category_names[2]
    }

# 테스트
# location = '이태원1동'
# result = get_use_money(location)
#print(result)