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
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)'
                ],
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
            },
            legend: {
                position: 'bottom', // 범례 위치 설정
                labels: {
                    fontSize: 14, // 범례 라벨 폰트 크기 설정
                    fontColor: '#333' // 범례 라벨 폰트 색상 설정
                }
            }
        }
    });
}

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
                backgroundColor: [
                    'rgba(163, 217, 155, 0.7)', // 목요일: 연한 올리브 그린
                    'rgba(92, 184, 92, 0.7)', // 월요일: 연한 녹색
                    'rgba(100, 158, 69, 0.7)', // 금요일: 진한 올리브 그린
                    'rgba(60, 118, 61, 0.7)', // 화요일: 중간 녹색
                    'rgba(32, 78, 32, 0.7)' // 수요일: 진한 녹색
                   
                    
                ],
            }],
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        min: 13, // 최소값 설정
                        max: 17, // 최대값 설정
                        precision: 0 // 세로축 소수점 제거
                    }
                }]
            },
            legend: {
                display: false // 범례 숨기기
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
                backgroundColor: [
                    'rgba(255, 205, 86, 0.7)', // 연한 노란색
                    'rgba(255, 159, 64, 0.7)', // 주황색
                    'rgba(75, 192, 192, 0.7)', // 청록색
                    'rgba(153, 102, 255, 0.7)', // 보라색
                    'rgba(255, 99, 132, 0.7)' // 붉은색
                ],
            }],
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        min: 10, // 최소값 설정
                        max: 17 // 최대값 설정
                    }
                }]
            },
            legend: {
                display: false // 범례 숨기기
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

function drawSimilarChart(data) {
    const quarters = Object.keys(data); // 분기
    const counts = Object.values(data); // 유사업종 수
    
    // 차트를 그릴 canvas 요소 가져오기
    const ctx = document.getElementById('myBarChart').getContext('2d');
    
    // 바 차트 생성
    new Chart(ctx, {
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