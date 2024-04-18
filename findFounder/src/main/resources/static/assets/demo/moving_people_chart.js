let weekendPieChart = null;
let weekdayBarChart = null;
let weekendBarChart = null;
// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
// 이전에 받아온 서버 응답 데이터를 바탕으로 그래프 그리기
function drawChart_moving(seoulMovingPeopleData) {
    // 전체 유동인구 데이터 가져오기
    const totalPopulation = seoulMovingPeopleData['전체유동인구'];

    // 주말과 주중 비율 데이터 가져오기
    const weekendPercentage = seoulMovingPeopleData['주말'];
    const weekdayPercentage = seoulMovingPeopleData['주중'];

    // 월요일부터 일요일까지의 유동인구 데이터 가져오기
    const weekdayData = [
        seoulMovingPeopleData['월요일'],
        seoulMovingPeopleData['화요일'],
        seoulMovingPeopleData['수요일'],
        seoulMovingPeopleData['목요일'],
        seoulMovingPeopleData['금요일']
    ];
    const weekendData = [
        seoulMovingPeopleData['토요일'],
        seoulMovingPeopleData['일요일']
    ];

    // 전체 유동인구를 보여주는 부분
    // const totalPopulationText = `해당 지역의 전체 유동인구수는 ${seoulMovingPeopleData['전체유동인구']}명 입니다.`;
    // const totalPopulationElement = document.getElementById('totalPopulation');
    // if (totalPopulationElement) {
    //     totalPopulationElement.textContent = totalPopulationText;
    // } else {
    //     console.error('totalPopulationElement이 존재하지 않습니다.');
    // }

    // 주말과 주중 비율을 보여주는 파이 그래프 그리기
    drawPieChart("movingPeopleChart", ['주말', '주중'], [weekendPercentage, weekdayPercentage]);

    // 월요일부터 일요일까지의 유동인구 비율을 보여주는 막대 그래프 그리기
    drawBarChart('movingPeopleChart3', ['월', '화', '수', '목', '금'], weekdayData);
    drawBarChart2('movingPeopleChart2', ['토', '일'], weekendData);
 
    
}

// 파이 그래프 그리기 함수
function drawPieChart(chartId, labels, data) {
    const ctx = document.getElementById(chartId).getContext('2d');
    if (weekendPieChart) {
        weekendPieChart.destroy(); // 이전에 생성된 차트 객체 삭제
    }
    weekendPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: ['#007bff', '#28a745'],
            }],
        },
    });
}

// 막대 그래프 그리기 함수
function drawBarChart(chartId, labels, data) {
    const ctx = document.getElementById(chartId).getContext('2d');
    if (weekdayBarChart) {
        weekdayBarChart.destroy(); // 이전에 생성된 차트 객체 삭제
    }
    

    weekdayBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '유동인구',
                data: data,
                backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#f8f9fa'],
            }],
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    min:minData-1, // 최소값 설정
                    max:maxData+1  // 최대값 설정
                }
            }
        }
    });
}
function drawBarChart2(chartId, labels, data) {
    const ctx = document.getElementById(chartId).getContext('2d');
    if (weekendBarChart) {
        weekendBarChart.destroy(); // 이전에 생성된 차트 객체 삭제
    }

    weekendBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '유동인구',
                data: data,
                backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#f8f9fa'],
            }],
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    min: 10, // 최소값 설정
                    max:20  // 최대값 설정
                }
            }
        }
    });
}