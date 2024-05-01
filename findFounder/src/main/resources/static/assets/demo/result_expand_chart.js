Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
Chart.defaults.color = 'rgba(0, 0, 0, 0)'; // 전역적으로 모든 그래프의 배경색을 투명하게 설정합니다.

function drawExpandChart(gu, dong, data, data2) {
    // 날짜 배열과 값 배열 생성
    let dates = Object.keys(data);
    const values = Object.values(data);
    const values2 = Object.values(data2);

    // 예측 시작 인덱스 정의 (2023년 3월)
    const predictionStartIndex = dates.findIndex(date => date === '20234');

    // 마지막 날짜 제거
    dates = dates.slice(0, -1);

    // 차트 그리기
    const ctx = document.getElementById('dongExpandChart').getContext('2d');
    ctx.canvas.style.backgroundColor = 'transparent'; // 캔버스 배경을 투명으로 설정

    const chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: dong+' 월평균 소비 예상',
                data: values,
                borderColor: 'red', // 첫 번째 그래프 색상
                //backgroundColor: 'rgba(255, 0, 0, 0.00)',
                tension: 0.1
            }, {
                label: gu+' 월평균 소비 예상',
                data: values2,
                borderColor: 'blue', // 두 번째 그래프 색상
                //backgroundColor: 'rgba(0, 0, 255, 0.00)',
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '년도와 분기'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: '월평균 소비'
                    }
                }
            }
        }
    });
}
