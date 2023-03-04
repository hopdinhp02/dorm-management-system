
function getDaysInMonth(year, month) {
    return new Date(year, month, 0).getDate();
}

async function getAllBranchsMoneybyDaysInMonth(moneyType, month, year) {
    let url = `http://localhost:8081/api/v1/profit/all-branch/${moneyType}/days-in-month?month=${month}&year=${year}`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    });
    const dataJson = await response.json();
    data = dataJson.data;
    return data;

}

async function getAllBranchsMoneybyMonthsInYear(moneyType, year) {
    let url = `http://localhost:8081/api/v1/profit/all-branch/${moneyType}/months-in-year?year=${year}`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    });
    const dataJson = await response.json();
    data = dataJson.data;
    return data;

}


getAllBranchsMoneybyDaysInMonth("revenue", 2, 2023)
async function genChart() {
    chartCanvas = document.getElementById('monthLineChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }
    chartCanvas = document.getElementById('yearLineChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }
    chartCanvas = document.getElementById('monthDoughnutChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }
    chartCanvas = document.getElementById('yearPolarAreaChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }

    date = document.getElementById("month");
    const monthYear = date.value.split('-');
    const year = monthYear[0];
    const month = monthYear[1];

    const monthLabels = [];
    const yearLabels = [];

    const daysRevenueArr = await getAllBranchsMoneybyDaysInMonth("revenue", month, year);
    const daysDepreciationArr = await getAllBranchsMoneybyDaysInMonth("depreciation", month, year);
    const daysMaintenanceArr = await getAllBranchsMoneybyDaysInMonth("maintenance", month, year);

    const monthsRevenueArr = await getAllBranchsMoneybyMonthsInYear("revenue", year);
    const monthsDepreciationArr = await getAllBranchsMoneybyMonthsInYear("depreciation", year);
    const monthsMaintenanceArr = await getAllBranchsMoneybyMonthsInYear("maintenance", year);


    try {
        monthRevenue = daysRevenueArr.reduce((a, b) => a + b, 0)
        monthDepreciation = daysDepreciationArr.reduce((a, b) => a + b, 0)
        monthMaintenance = daysMaintenanceArr.reduce((a, b) => a + b, 0)
        monthProfit = monthRevenue - monthDepreciation - monthMaintenance

    } catch (error) {
        monthRevenue = 0
        monthDepreciation = 0
        monthMaintenance = 0
        monthProfit = 0


    }

    try {
        yearRevenue = monthsRevenueArr.reduce((a, b) => a + b, 0)
        yearDepreciation = monthsDepreciationArr.reduce((a, b) => a + b, 0)
        yearMaintenance = monthsMaintenanceArr.reduce((a, b) => a + b, 0)
        yearProfit = yearRevenue - yearDepreciation - yearMaintenance
    } catch (error) {
        yearRevenue = 0
        yearDepreciation = 0
        yearMaintenance = 0
        yearProfit = 0
    }


    for (let i = 0; i < getDaysInMonth(year, month); i++) {
        monthLabels[i] = i + 1;
    }
    for (let i = 0; i < 12; i++) {
        yearLabels[i] = i + 1;
    }
    const monthLineData = {
        labels: monthLabels,
        datasets: [
            {
                label: 'Revenue',
                backgroundColor: '#3333ff',
                borderColor: '#3333ff',
                data: daysRevenueArr,
            },
            {
                label: 'Depreciation',
                data: daysDepreciationArr,
                backgroundColor: '#ff6600',
                borderColor: '#ff6600',
            }
            ,
            {
                label: 'Maintenance',
                data: daysMaintenanceArr,
                backgroundColor: '#00cc00',
                borderColor: '#00cc00',
            }
        ]
    };
    const yearLineData = {
        labels: yearLabels,
        datasets: [
            {
                label: 'Revenue',
                backgroundColor: '#3333ff',
                borderColor: '#3333ff',
                data: monthsRevenueArr,
            },
            {
                label: 'Depreciation',
                data: monthsDepreciationArr,
                backgroundColor: '#ff6600',
                borderColor: '#ff6600',
            }
            ,
            {
                label: 'Maintenance',
                data: monthsMaintenanceArr,
                backgroundColor: '#00cc00',
                borderColor: '#00cc00',
            }
        ]
    };

    const monthLineConfig = {
        type: 'line',
        data: monthLineData,
        options: {
            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            stacked: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Financial Situation by days in month'
                }
            }
        }
    };

    const yearLineConfig = {
        type: 'line',
        data: yearLineData,
        options: {
            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            stacked: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Financial Situation by months in years'
                }
            }
        }
    };

    const monthLineChart = new Chart(
        document.getElementById('monthLineChart'),
        monthLineConfig
    );
    const yearLineChart = new Chart(
        document.getElementById('yearLineChart'),
        yearLineConfig
    );
    const yearPolarAreaData = {
        labels: [
            'Profit',
            'revenue',
            'Depreciation',
            'maintenance'
        ],
        datasets: [{
            //   label: 'My First Dataset',
            data: [yearProfit, yearRevenue, yearDepreciation, yearMaintenance],
            backgroundColor: [
                'rgb(255, 99, 132)',
                '#3333ff',
                '#ff6600',
                '#00cc00'
            ]
        }]
    };
    const yearPolarAreaConfig = {
        type: 'polarArea',
        data: yearPolarAreaData,
        options: {
            plugins: {
                title: {
                    display: true,
                    text: 'Financial Situation in year'
                }
            }
        }
    };
    const yearPolarAreaChart = new Chart(
        document.getElementById('yearPolarAreaChart'),
        yearPolarAreaConfig
    );

    const monthDoughnutData = {
        labels: [
            'revenue',
            'Depreciation',
            'maintenance'
        ],
        datasets: [{
            data: [monthRevenue, monthDepreciation, monthMaintenance],
            backgroundColor: [
                '#3333ff',
                '#ff6600',
                '#00cc00'
            ],
            hoverOffset: 4
        }]
    };
    const monthDoughnutConfig = {
        type: 'doughnut',
        data: monthDoughnutData,
        options: {
            plugins: {
                title: {
                    display: true,
                    text: 'Financial Situation in month'
                }
            }
        }
    };
    const monthPolarAreaChart = new Chart(
        document.getElementById('monthDoughnutChart'),
        monthDoughnutConfig
    );


    // const monthPolarAreaData = {
    //     labels: [
    //       'Profit',
    //       'revenue',
    //       'Depreciation',
    //       'maintenance'
    //     ],
    //     datasets: [{
    //     //   label: 'My First Dataset',
    //       data: [monthProfit, monthRevenue, monthDepreciation, monthMaintenance],
    //       backgroundColor: [
    //         'rgb(255, 99, 132)',
    //         '#3333ff',
    //         '#ff6600',
    //         '#00cc00'
    //       ]
    //     }]
    //   };
    // const monthPolarAreaConfig = {
    //     type: 'polarArea',
    //     data: monthPolarAreaData,
    //     options: {}
    //   };
    //   const monthPolarAreaChart = new Chart(
    //     document.getElementById('monthPolarAreaChart'),
    //     monthPolarAreaConfig
    // );
}

const currDate = new Date();
const currYear = currDate.getFullYear();
const currMonth = currDate.getMonth();
const MonthInput = document.getElementById("month");
if (currMonth >= 10) {
    MonthInput.value = currYear + "-" + currMonth;
} else {
    MonthInput.value = currYear + "-0" + currMonth;
}

// document.getElementById("month").value = `${currDate.getFullYear()}-${currDate.getMonth()}`;
// console.log(document.getElementById("month").value)
genChart();




