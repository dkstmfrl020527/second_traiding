
# 모듈 임포트 
import os
import sys

# ⭐ JAVA_HOME을 맨 처음에 설정 (다른 import 전에!)
os.environ['JAVA_HOME'] = r'C:\Program Files\Java\jdk-21'

import requests
from konlpy.tag import Okt  # 이제 Java를 찾을 수 있음
from collections import Counter
from bs4 import BeautifulSoup as bs
from wordcloud import WordCloud
from time import strftime
import json

path= sys.argv[2]

# 원격 컨텐츠 로드 
url = sys.argv[1] if len(sys.argv) > 2 else "https://news.naver.com/"


html = requests.get(url).text
html = requests.get(url).text
soup = bs(html, 'html.parser')
body = soup.select_one("body")
text = body.get_text().strip().replace("\n", " ")
stopwords = ['본문', '바로가기', 'NAVER', '검색', '이슈', '닫기', '구독','보기','더','뉴스']

# 명사, 형용사, 동사의 단어로 형태소 분리
okt = Okt()  # 이제 에러 안 날 거예요!
words = []
for word, pos in okt.pos(text):
    if word not in stopwords and pos in ['Noun', 'Verb', 'Adjective']:
        words.append(word)

# 가장 많이 등장하는 키워드를 상위 50개 추출 
stat = Counter(words).most_common(50)

# 워드 클라우드 이미지 생성 
wc = WordCloud(font_path=r'.\trend\NanumGothic-ExtraBold.ttf',
               background_color='white', 
               max_font_size=100,                                                          
               width=500, height=300)
cloud = wc.generate_from_frequencies(dict(stat))
filename = f"{strftime('%Y%m%d%H')}_news.jpg"
cloud.to_file(f"{path}/{filename}")


# ⭐ Java에서 받을 JSON 출력 (중요!)
result = {
    "image": filename,
    "keywords": dict(stat)  # 상위 20개 키워드
}
print(json.dumps(result, ensure_ascii=False))  # 한글 지원

