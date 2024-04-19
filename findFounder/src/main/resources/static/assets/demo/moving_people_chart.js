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

    // 주말과 주중 비율을 보여주는 파이 그래프 그리기
    drawPieChart("movingPeopleChart", ['주말', '주중'], [weekendPercentage, weekdayPercentage]);

    // 월요일부터 일요일까지의 유동인구 비율을 보여주는 막대 그래프 그리기
    drawBarChart('movingPeopleChart3', ['월', '화', '수', '목', '금'], weekdayData);
    drawBarChart2('movingPeopleChart2', ['토', '일'], weekendData);
 
    
    // 유동인구가 가장 많은 요일 표시
    const mostPopularDay = findMostPopularDay(weekdayData.concat(weekendData));
    drawMostPopularDay(mostPopularDay);

    // 일일평균 유동인구 표시
    displayTotalPopulation(totalPopulation);
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
        options: {
            tooltips: {
                callbacks: {
                    label: function(tooltipItem, data) {
                        var dataset = data.datasets[tooltipItem.datasetIndex];
                        var total = dataset.data.reduce(function(previousValue, currentValue, currentIndex, array) {
                            return previousValue + currentValue;
                        });
                        var currentValue = dataset.data[tooltipItem.index];
                        var percentage = Math.floor(((currentValue / total) * 100) + 0.5);  
                        return percentage + "%";
                    }
                }
            }
        }
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
                label: '주중 유동인구',
                data: data,
                backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#f8f9fa'],
            }],
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        min: 12, // 최소값 설정
                        max: 16 // 최대값 설정
                    }
                }]
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
                label: '주말 유동인구',
                data: data,
                backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#f8f9fa'],
            }],
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        min: 12, // 최소값 설정
                        max: 16 // 최대값 설정
                    }
                }]
            }
        }
    });
}

// 유동인구가 가장 많은 요일을 찾는 함수
function findMostPopularDay(data) {
    // 요일과 그에 해당하는 유동인구 데이터를 묶음
    const days = ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'];
    const dayData = days.map((day, index) => ({
        day: day,
        population: data[index]
    }));

    // 유동인구가 가장 많은 요일 찾기
    let mostPopularDay = dayData[0];
    for (let i = 1; i < dayData.length; i++) {
        if (dayData[i].population > mostPopularDay.population) {
            mostPopularDay = dayData[i];
        }
    }

    return mostPopularDay.day;
}

// 유동인구가 가장 많은 요일을 표시하는 함수
function drawMostPopularDay(day) {
    const mostPopularDayElement = document.getElementById('mostPopularDay');
    if (mostPopularDayElement) {
        mostPopularDayElement.textContent = day;
    } else {
        console.error('mostPopularDayElement이 존재하지 않습니다.');
    }
}
// 일일평균 유동인구 표시 함수
function displayTotalPopulation(population) {
    const totalPopulationElement = document.getElementById('totalPopulation');
    if (totalPopulationElement) {
        totalPopulationElement.textContent = population.toLocaleString(); // 적절한 형식으로 숫자 표시
    } else {
        console.error('totalPopulationElement이 존재하지 않습니다.');
    }
}