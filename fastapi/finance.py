import pandas as pd
from pandas_datareader import data as pdr
import sqlalchemy

code_list = [['카카오', '035720.KS'], ['삼성전자', '005930.KS'], ['카카오뱅크', '323410.KS'], ['롯데정보통신', '286940.KS'],
             ['네이버', '035420.KS'], ['SK하이닉스', '000660.KS'], ['LG전자', '066570.KS'], ['현대차', '005380.KS'],
             ['기아', '000270.KS'], ['KT&G', '033780.KS'], ['셀트리온', '068270.KS'], ['LG에너지솔루션', '373220.kS'],
             ['대한항공', '003490.KS'], ['JW생명과학', '234080.KS'], ['LIG넥스원', '079550.KS'], ['POSCO홀딩스', '005490.KS']]
code_logo = {
    '카카오': 'https://img1.daumcdn.net/thumb/C400x400/?fname=http://t1.daumcdn.net/brunch/service/user/41jj/image/XsgnrTPMROwS8juOxy7oVokVqyg.jpg',
    '삼성전자': 'http://wiki.hash.kr/images/4/4c/%EC%82%BC%EC%84%B1%EA%B7%B8%EB%A3%B9_%EB%A1%9C%EA%B3%A0.png',
    '카카오뱅크': 'https://upload.wikimedia.org/wikipedia/commons/5/52/Kakao_Bank_of_Korea_Logo.jpg',
    '롯데정보통신': 'https://cdn.jumpit.co.kr/images/dabin_3358/20213410103423385_245_247.png',
    '네이버': 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRGC7p9kBthTh6x4uNoLqMO2hinJHCooEcEw&usqp=CAU',
    'SK하이닉스': 'https://www.sk.co.kr/lib/images/desktop/about/ci-motif-img01_lg.png',
    'LG전자': 'https://www.lgdisplay.com/images/icon/icon_symbor_mark.png',
    '현대차': 'http://wiki.hash.kr/images/2/2b/%ED%98%84%EB%8C%80%EC%9E%90%EB%8F%99%EC%B0%A8%E3%88%9C_%EB%A1%9C%EA%B3%A0.png',
    '기아': 'https://mblogthumb-phinf.pstatic.net/MjAxOTEyMjdfMjI1/MDAxNTc3NDQ0ODc0MTIz.ypZvwbNYgyOWzmNxfBhKOZyzHfaY0Dv7oTUmihsilKYg.5_g9NQERDM44FBKbldkaTCNmJ6eQChJ1LIGTdIs0AHIg.PNG.dailybrand/kia.png?type=w800',
    'KT&G': 'https://mblogthumb-phinf.pstatic.net/20160921_93/ppanppane_1474428477730NiNkP_PNG/%C4%C9%C0%CC%C6%BC%BF%A3%C1%F6_KTG_%B7%CE%B0%ED_%282%29.png?type=w800',
    '셀트리온': 'https://s3-symbol-logo.tradingview.com/celltrion--600.png',
    'LG에너지솔루션': 'http://wiki.hash.kr/images/f/f1/%EC%97%98%EC%A7%80%EA%B7%B8%EB%A3%B9_%EB%A1%9C%EA%B3%A0.png',
    '대한항공': 'http://wiki.hash.kr/images/d/d8/%E3%88%9C%EB%8C%80%ED%95%9C%ED%95%AD%EA%B3%B5_%EB%A1%9C%EA%B3%A0.png',
    'JW생명과학': 'https://cdn.kmpnews.co.kr/news/photo/201101/4727_1014_4902.jpg',
    'LIG넥스원': 'https://image.thebell.co.kr/news/photo/2021/10/06/20211006091824128.JPG',
    'POSCO홀딩스': 'https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcKRr2c%2FbtrAmAz0sgY%2F7TSDjLiLAIsm3t61RYv7Ek%2Fimg.jpg'}


def finance_create(engine):
    finance = pd.DataFrame()

    for name, code in code_list:
        temp = pdr.get_data_yahoo(code, start="2020-01-01")
        temp.rename(columns={'High': 'high', 'Low': 'low', 'Open': 'open', 'Close': 'close', 'Volume': 'volume'},
                    inplace=True)
        temp.drop(['Adj Close'], axis=1, inplace=True)
        temp['fn_name'] = name
        temp['fn_logo'] = code_logo[name]
        temp['fn_date'] = temp.index.strftime('%Y-%m-%d')
        temp['per'] = round(((temp.iloc[-1, 3] - temp.iloc[-2, 3]) / temp.iloc[-1, 3]) * 100, 2)
        finance = pd.concat([finance, temp], ignore_index=True)

    finance['id'] = finance.index + 1
    dtypesql = {
        'id': sqlalchemy.types.Integer(),
        'fn_name': sqlalchemy.types.VARCHAR(12),
        'fn_logo': sqlalchemy.types.VARCHAR(197),
        'fn_date': sqlalchemy.types.Date(),
        'open': sqlalchemy.types.Integer(),
        'close': sqlalchemy.types.Integer(),
        'high': sqlalchemy.types.Integer(),
        'low': sqlalchemy.types.Integer(),
        'volume': sqlalchemy.types.Integer(),
        'per': sqlalchemy.types.Float()
    }

    finance.to_sql(name='finance', con=engine, if_exists='replace', index=False, dtype=dtypesql)
