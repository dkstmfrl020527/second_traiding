window.addEventListener("DOMContentLoaded", function() {
    try {
        /* 트렌드 통계 데이터 처리 S */
        const chartDataElement = document.getElementById("chart-data");

        // 요소가 존재하는지 확인
        if (!chartDataElement) {
            console.error('chart-data 요소를 찾을 수 없습니다');
            return;
        }

        let data = chartDataElement.innerHTML.trim();
        console.log('원본 데이터:', data);

        // 빈 데이터 체크
        if (!data) {
            console.error('차트 데이터가 비어있습니다');
            return;
        }

        // JSON 파싱
        data = JSON.parse(data);
        console.log('파싱된 데이터:', data);

        const labels = Object.keys(data);
        const values = Object.values(data);

        console.log('라벨:', labels);
        console.log('값:', values);

        // 데이터가 있는지 확인
        if (labels.length === 0) {
            console.error('키워드 데이터가 없습니다');
            return;
        }

        /* 트렌드 통계 데이터 처리 E */

        // 차트 캔버스 요소 확인
        const ctx = document.getElementById('myChart');
        if (!ctx) {
            console.error('myChart 캔버스를 찾을 수 없습니다');
            return;
        }

        // 도넛 차트 색상 배열
        const colors = [
            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
            '#FF9F40', '#FF6384', '#C9CBCF', '#4BC0C0', '#FF6384',
            '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40',
            '#FF6384', '#C9CBCF', '#4BC0C0', '#FF6384', '#36A2EB'
        ];

        // 차트 생성
        new Chart(ctx, {
            type: 'doughnut', // pie에서 doughnut으로 변경
            data: {
                labels: labels,
                datasets: [{
                    label: '핫 트렌드',
                    data: values,
                    backgroundColor: colors.slice(0, labels.length),
                    borderColor: '#fff',
                    borderWidth: 2,
                    hoverBorderWidth: 3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            padding: 20,
                            usePointStyle: true,
                            font: {
                                size: 12
                            }
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
                cutout: '50%' // 도넛 구멍 크기
                // scales 옵션 제거 (pie/doughnut 차트에는 불필요)
            }
        });

        console.log('차트 생성 완료');

    } catch (error) {
        console.error('차트 생성 중 오류 발생:', error);

        // 에러 정보를 화면에 표시
        const chartContainer = document.getElementById('myChart');
        if (chartContainer && chartContainer.parentNode) {
            const errorDiv = document.createElement('div');
            errorDiv.style.color = 'red';
            errorDiv.style.padding = '20px';
            errorDiv.style.border = '1px solid red';
            errorDiv.style.borderRadius = '5px';
            errorDiv.innerHTML = `
                <h3>차트 로드 오류</h3>
                <p>오류: ${error.message}</p>
                <p>개발자 도구(F12)를 열어 자세한 정보를 확인하세요.</p>
            `;
            chartContainer.parentNode.insertBefore(errorDiv, chartContainer);
        }
    }
});