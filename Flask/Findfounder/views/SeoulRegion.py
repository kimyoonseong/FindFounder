import pandas as pd

def remove_word(df, column, word):
    df[column] = df[column].str.replace(word, '')
    return df

def read_region_from_csv(category):
    region_df1 = pd.read_csv('views\csvFolder\seoul.csv', encoding='utf-8')

    # 빈 열 제거
    region_df1 = region_df1.dropna(axis=1, how='all')
    # 빈 행 제거
    region_df1 = region_df1.dropna(axis=0, how='all')
    region_df1 = region_df1.drop_duplicates(subset=['행정동코드', '행정동'])

    # "제"라는 부분 삭제
    region_df1 = remove_word(region_df1, '행정동', '제')

    # 자치구에 해당하는 행정동 추출
    subdistricts = region_df1[region_df1['자치구'] == category]['행정동'].tolist()

    return subdistricts

# 테스트

# category='송파구'
# statistics = read_region_from_csv(category)
# print(statistics)