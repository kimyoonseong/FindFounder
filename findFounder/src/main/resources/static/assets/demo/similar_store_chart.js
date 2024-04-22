Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
let myBarChart = null; // 전역 변수로 이전에 생성된 차트 객체를 저장합니다.

function drawSimilarChart(seoulSimilarStore) {
    const quarters = Object.keys(seoulSimilarStore); // 분기
    const counts = Object.values(seoulSimilarStore); // 유사업종 수
    
    // 차트를 그릴 canvas 요소 가져오기
    const ctx = document.getElementById('myBarChart').getContext('2d');

    // 이전에 생성된 차트 객체가 있다면 삭제합니다.
    if (myBarChart) {
        myBarChart.destroy();
    }

    // 새로운 차트를 생성하고 저장합니다.
    myBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: quarters.map(quarter => `2023 ${quarter.charAt(4)}분기`), // 분기 라벨 설정
            datasets: [{
                label: '유사업종 수',
                backgroundColor: 'rgba(54, 162, 235, 0.2)', // 바 색상 설정
                borderColor: 'rgba(54, 162, 235, 1)', // 바 테두리 색상 설정
                borderWidth: 1,
                data: counts // 유사업종 수 데이터 설정
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true // 세로축이 0부터 시작하도록 설정
                    }
                }]
            }
        }
    });
}
