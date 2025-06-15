## 개요 ##
Spring Boot 기반의 실시간 트렌드 분석 시스템입니다. 웹사이트의 뉴스 및 콘텐츠를 분석하여 키워드 트렌드를 시각화합니다.

## 기능 ##
파이썬을 이용한 사용자 입력 url 웹 크롤링
키워드 추출 및 워드 클라우드 시각화
sql 연동으로 데이터 저장
Chart.js를 활용한 키워드 빈도 차트
~~일주일간 키워듸 변동 그래프~~

## 환경 요구사항 ##
- Java 17+
- Python 3.8+
- MySQL 8.0+
- pip install wordcloud matplotlib pandas beautifulsoup4

## 데이터 베이스 설정 ##
CREATE DATABASE trend_db;

CREATE TABLE TREND (
    seq BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255),
    wordCloud VARCHAR(500),
    keywords TEXT,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifiedAt DATETIME,
    deletedAt DATETIME
);

