Chart.defaults.global.defaultFontFamily = '-apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
Chart.defaults.color = 'rgba(0, 0, 0, 0)'; // 전역적으로 모든 그래프의 배경색을 투명하게 설정합니다.

function drawexpectsales(sales) {
    const chartContainer = document.getElementById('chartContainer');
    const chartRow = document.createElement('div');
    chartRow.classList.add('chart-row');
    chartContainer.appendChild(chartRow);

    let chartCount = 0;

    Object.entries(sales).forEach(([industry, data], index) => {
        // 순위에 해당하는 업종은 무시
        if (!isNaN(industry)) {
            return;
        }

        // 새로운 차트 컨테이너를 만들어 각 차트를 넣습니다. 이 컨테이너는 3개의 열로 고정됩니다.
        let currentChartContainer;
        if (chartCount % 3 === 0) {
            currentChartContainer = document.createElement('div');
            currentChartContainer.classList.add('chart-container');
            chartRow.appendChild(currentChartContainer);
        } else {
            currentChartContainer = chartRow.children[Math.floor(chartCount / 3)];
        }

        const canvas = document.createElement('canvas');
        canvas.width = 300; // 임시값
        canvas.height = 200;

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
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        currentChartContainer.appendChild(canvas);
        chartCount++;
    });
}
