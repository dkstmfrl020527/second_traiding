window.addEventListener("DOMContentLoaded", function() {
    console.log('=== DOMContentLoaded 시작 ===');

    // 실시간 파이 차트 생성
    createRealtimePieChart();

    // 일주일 라인 차트 생성
    createWeeklyLineChart();
});

// 📊 실시간 파이(도넛) 차트 생성
function createRealtimePieChart() {
    console.log('=== 실시간 파이 차트 생성 시작 ===');

    try {
        /* 실시간 트렌드 데이터 처리 S */
        const chartDataElement = document.getElementById("chart-data");

        // 요소가 존재하는지 확인
        if (!chartDataElement) {
            console.log('❌ chart-data 요소를 찾을 수 없습니다 (실시간 데이터 없음)');
            return;
        }

        let data = chartDataElement.innerHTML.trim();
        console.log('📊 실시간 원본 데이터:', data);

        // 빈 데이터 체크
        if (!data) {
            console.error('❌ 실시간 차트 데이터가 비어있습니다');
            return;
        }

        // JSON 파싱
        data = JSON.parse(data);
        console.log('✅ 실시간 파싱된 데이터:', data);

        const labels = Object.keys(data);
        const values = Object.values(data);

        console.log('🏷️ 실시간 라벨:', labels);
        console.log('📈 실시간 값:', values);

        // 데이터가 있는지 확인
        if (labels.length === 0) {
            console.error('❌ 실시간 키워드 데이터가 없습니다');
            return;
        }

        /* 실시간 트렌드 데이터 처리 E */

        // 차트 캔버스 요소 확인
        const ctx = document.getElementById('myChart');
        if (!ctx) {
            console.error('❌ myChart 캔버스를 찾을 수 없습니다');
            return;
        }

        // 도넛 차트 색상 배열
        const pieColors = [
            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
            '#FF9F40', '#FF6384', '#C9CBCF', '#4BC0C0', '#FF6384',
            '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40',
            '#FF6384', '#C9CBCF', '#4BC0C0', '#FF6384', '#36A2EB'
        ];

        // 실시간 도넛 차트 생성
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    label: '실시간 핫 트렌드',
                    data: values,
                    backgroundColor: pieColors.slice(0, labels.length),
                    borderColor: '#fff',
                    borderWidth: 2,
                    hoverBorderWidth: 3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    title: {
                        display: true,
                        text: '📊 실시간 키워드 분석',
                        font: { size: 16 }
                    },
                    legend: {
                        position: 'right',
                        labels: {
                            padding: 20,
                            usePointStyle: true,
                            font: { size: 12 }
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.parsed;
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = ((value / total) * 100).toFixed(1);
                                return `${label}: ${value}회 (${percentage}%)`;
                            }
                        }
                    }
                },
                cutout: '50%'
            }
        });

        console.log('✅ 실시간 파이 차트 생성 완료');

    } catch (error) {
        console.error('❌ 실시간 파이 차트 생성 중 오류 발생:', error);
        showChartError('myChart', error, '실시간 파이 차트');
    }
}

function getTopKeywords(keywordsByDate, limit = 5) {
    const allKeywords = {};

    keywordsByDate.forEach(keywords => {
        Object.entries(keywords || {}).forEach(([keyword, count]) => {
            allKeywords[keyword] = (allKeywords[keyword] || 0) + count;
        });
    });

    return Object.entries(allKeywords)
        .sort((a, b) => b[1] - a[1])
        .slice(0, limit)
        .map(([keyword]) => keyword);
}

// 📈 라인 차트 데이터셋 생성 함수
function createLineDatasets(topKeywords, keywordsByDate) {
    const lineColors = [
        'rgb(255, 99, 132)',   // 빨강
        'rgb(54, 162, 235)',   // 파랑
        'rgb(255, 205, 86)',   // 노랑
        'rgb(75, 192, 192)',   // 청록
        'rgb(153, 102, 255)'   // 보라
    ];

    return topKeywords.map((keyword, index) => {
        const data = keywordsByDate.map(keywords => (keywords || {})[keyword] || 0);

        return {
            label: keyword,
            data: data,
            borderColor: lineColors[index % lineColors.length],
            backgroundColor: lineColors[index % lineColors.length] + '20',
            tension: 0.4,
            fill: false,
            pointRadius: 5,
            pointHoverRadius: 7
        };
    });
}


// 📈 일주일 라인 차트 생성
function createWeeklyLineChart() {
    console.log('=== 일주일 라인 차트 생성 시작 ===');

    try {
        /* 일주일 트렌드 데이터 처리 S */
        const datesElement = document.getElementById("weekly-dates");
        const keywordsElement = document.getElementById("weekly-keywords");

        if (!datesElement || !keywordsElement) {
            console.log('❌ 일주일 트렌드 데이터 요소 없음 (weekly-dates 또는 weekly-keywords 없음)');
            return;
        }

        const datesText = datesElement.textContent.trim();
        const keywordsText = keywordsElement.textContent.trim();

        console.log('📅 일주일 날짜 원본:', datesText);
        console.log('📊 일주일 키워드 원본 (처음 200자):', keywordsText.substring(0, 200) + '...');

        if (!datesText || !keywordsText) {
            console.log('❌ 일주일 트렌드 데이터가 비어있음');
            return;
        }

        const dates = JSON.parse(datesText);
        const keywordsByDate = JSON.parse(keywordsText);

        console.log('✅ 일주일 파싱 성공');
        console.log('📅 일주일 날짜 배열:', dates);
        console.log('📊 일주일 키워드 배열 길이:', keywordsByDate.length);
        console.log('📊 첫 번째 날짜 키워드:', keywordsByDate[0]);

        if (!dates.length || !keywordsByDate.length) {
            console.log('❌ 파싱된 일주일 데이터가 비어있음');
            return;
        }

        /* 일주일 트렌드 데이터 처리 E */

        // 캔버스 요소 확인
        const weeklyCtx = document.getElementById('weeklyTrendChart');
        if (!weeklyCtx) {
            console.log('❌ weeklyTrendChart 캔버스 없음');
            return;
        }

        // 상위 키워드 추출
        const topKeywords = getTopKeywords(keywordsByDate, 5);
        console.log('🏆 상위 5개 키워드:', topKeywords);

        // 차트 데이터셋 생성
        const datasets = createLineDatasets(topKeywords, keywordsByDate);
        console.log('📈 라인 데이터셋 생성 완료');

        // 일주일 라인 차트 생성
        new Chart(weeklyCtx, {
            type: 'line',
            data: {
                labels: dates,
                datasets: datasets
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: '📈 주요 키워드 트렌드 변화 (최근 7일)',
                        font: { size: 16 }
                    },
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '언급 빈도'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: '날짜'
                        }
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index'
                }
            }
        });

        console.log('✅ 일주일 라인 차트 생성 완료');

    } catch (error) {
        console.error('❌ 일주일 라인 차트 생성 중 오류:', error);
        showChartError('weeklyTrendChart', error, '일주일 라인 차트');
    }
}