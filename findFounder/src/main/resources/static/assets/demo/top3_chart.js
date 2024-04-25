function drawTopThreeIndustries(sales, recommendedIndustry, recommendedIndustry2, recommendedIndustry3) {
    // Recommended industries
    const industries = [recommendedIndustry, recommendedIndustry2, recommendedIndustry3];
    const chartsContainer = document.getElementById('charts-container');

    // 각 업종에 대한 차트 그리기
    industries.forEach((industry, index) => {
        if (sales[industry]) {
            const industryData = Object.values(sales[industry]);
            const industryLabels = Object.keys(sales[industry]);

            // 차트 컨테이너 생성
            const chartContainer = document.createElement('div');
            chartContainer.className = 'chart-container';
            chartsContainer.appendChild(chartContainer);

            // 캔버스 생성
            const canvas = document.createElement('canvas');
            canvas.id = `chart-${index}`;
            chartContainer.appendChild(canvas);

            // 차트 생성
            const ctx = canvas.getContext('2d');
            const myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: industryLabels,
                    datasets: [{
                        label: industry,
                        data: industryData,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1,
                        fill: false
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    },
                    // 막대의 폭 조절
                    barThickness: 10 // 막대의 폭을 픽셀 단위로 지정
                }
            });

            // 1위, 2위, 3위 표시
            const chartTitle = document.createElement('div');
            chartTitle.className = 'chart-title';
            chartTitle.textContent = `${index + 1}위 - ${industry}`;
            chartContainer.appendChild(chartTitle);
        }
    });
}
