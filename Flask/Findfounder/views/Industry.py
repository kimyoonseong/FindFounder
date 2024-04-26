import pandas as pd
import csv

#상업 리스트
def read_industry_from_csv(category):
    industry_df = pd.read_csv('views\csvFolder\IndustryList.csv')
    # industry_list = []
    industry_list = industry_df[category][industry_df[category].notna()].to_list()
    # with open('views/IndustryList.csv', newline='', encoding='utf-8') as csvfile:
    #     reader = csv.DictReader(csvfile)
    #     for row in reader:
    #         if row.get(category):  # 주어진 카테고리의 열 값이 존재하는지 확인
    #             industry_list.append(row[category])
    return industry_list