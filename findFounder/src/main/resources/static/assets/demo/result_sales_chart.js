Chart.defaults.global.defaultFontFamily = '-apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
Chart.defaults.color = 'rgba(0, 0, 0, 0)'; // 전역적으로 모든 그래프의 배경색을 투명하게 설정합니다.

function drawexpectsales(sales) {
    const chartContainer = document.getElementById('chartContainer');
    chartContainer.innerHTML = ''; // 기존 차트를 모두 지웁니다.

    const numColumns = 3; // 한 행당 최대 열 개수
    const chartWidth = Math.floor(window.innerWidth / numColumns) - 80; // 차트의 너비는 화면 폭을 기준으로 조절합니다.
    const chartHeight = 300; // 차트의 고정 높이

    let chartCount = 0;

    for (const [industry, data] of Object.entries(sales)) {
        if (!isNaN(industry) || chartCount >= 10) {
            continue;
        }

        // 새로운 차트 컨테이너를 만들고 해당 열에 추가합니다.
        if (chartCount % numColumns === 0) {
            var chartRow = document.createElement('div');
            chartRow.classList.add('chart-row');
            chartContainer.appendChild(chartRow);
        }

        const canvas = document.createElement('canvas');
        canvas.width = chartWidth;
        canvas.height = chartHeight;

        const ctx = canvas.getContext('2d');
        const chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: Object.keys(data),
                datasets: [{
                    label: industry,
                    data: Object.values(data),
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                }
            }
        });

        chartRow.appendChild(canvas);
        chartCount++;
    }
}

// // 창 크기가 변경될 때 차트를 다시 그리도록 이벤트 리스너 추가
// window.addEventListener('resize', () => {
//     drawexpectsales(sales);
// });
