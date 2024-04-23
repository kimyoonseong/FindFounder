import pandas as pd
def get_zipgack(signgu_cd_nm):
    if signgu_cd_nm.endswith('동'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_zipgack_dong.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\Seoul_zipgack_dong.csv', encoding='cp949')
        filter_col = 'ADSTRD_CD_NM'
    if signgu_cd_nm.endswith('구'):
        #df = pd.read_csv(r'C:\Users\82104\git\FindFounder\Flask\Findfounder\views\csvFolder\Seoul_zipgack_gu.csv', encoding='cp949')
        df = pd.read_csv('views\csvFolder\Seoul_zipgack_gu.csv', encoding='cp949')
        filter_col = 'SIGNGU_CD_NM'
    col_name = 'VIATR_FCLTY_CO'
    col_list = df.columns.to_list()
    avg = df['VIATR_FCLTY_CO'].mean()
    temp = df[col_list[:4]]
    temp2 = temp.loc[(temp['STDR_YYQU_CD']>20230) & (temp[filter_col]==signgu_cd_nm)]
    temp2.reset_index(inplace =  True)
    temp2['2023_avg'] = round(avg, 0)
    #temp2['avg_diff'] = (temp2['VIATR_FCLTY_CO'] - round(avg)) / round(avg) * 100
    temp2['avg_diff'] = (temp2['VIATR_FCLTY_CO']) / round(avg) * 100
    result_json = {"ADSTRD_CD_NM" : signgu_cd_nm, "avg_diff" : int(temp2.loc[temp2.shape[0]-1, 'avg_diff'])-100}# % 구하기위해 뺌
    #result_json = { "avg_diff":temp2.loc[temp2.shape[0]-1, 'avg_diff']}
    
    return dict(result_json)

# 테스트
# signgu_cd_nm = '금천구'
# result_json = get_zipgack(signgu_cd_nm)
# print(type(result_json))
# print(result_json)