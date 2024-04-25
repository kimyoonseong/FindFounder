import pandas as pd
import csv
def read_industry_from_csv(category):
    industry_df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Expend_eng_2017_01_01_2023_07_01.csv', encoding='cp949')
    # category에 해당하는 자료만 필터링합니다.
    category_df = industry_df[industry_df['SIGNGU_CD_NM'] == category]
    
    # STDR_YYQU_CD(해당분기)별 EXPNDTR_TOTAMT(총지출)의 합을 구합니다.
    quarterly_expndtr = category_df.groupby('STDR_YYQU_CD')['EXPNDTR_TOTAMT'].sum()
    print(quarterly_expndtr)
    
    return "1"

# 테스트
# signgu_cd_nm = '용산구'
# statistics = read_industry_from_csv(signgu_cd_nm)
# print(statistics)