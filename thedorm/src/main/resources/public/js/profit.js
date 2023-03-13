
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

async function getBranchMoneybyMonthsInYear(id, moneyType, year) {
    let url = `http://localhost:8081/api/v1/profit/branchs/${id}/${moneyType}/months-in-year?year=${year}`;
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

async function getDormMoneybyMonthsInYear(id, moneyType, year) {
    let url = `http://localhost:8081/api/v1/profit/dorms/${id}/${moneyType}/months-in-year?year=${year}`;
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

async function getRoomMoneybyMonthsInYear(id, moneyType, year) {
    let url = `http://localhost:8081/api/v1/profit/rooms/${id}/${moneyType}/months-in-year?year=${year}`;
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

async function getSlotMoneybyMonthsInYear(id, moneyType, year) {
    let url = `http://localhost:8081/api/v1/profit/slots/${id}/${moneyType}/months-in-year?year=${year}`;
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
// getAllBranchsMoneybyDaysInMonth("revenue", 2, 2023)
async function genLineChart() {
    chartCanvas = document.getElementById('yearBarChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }

    const year = document.getElementById("year").value;
    const yearLabels = [];
    for (let i = 0; i < 12; i++) {
        yearLabels[i] = i + 1;
    }
    var monthsRevenueArr = [];
        var monthsDepreciationArr = [];
        var monthsMaintenanceArr =[];
    let type = document.getElementById("show-by").value
    if(type == 0){
         monthsRevenueArr = await getAllBranchsMoneybyMonthsInYear("revenue", year);
        monthsDepreciationArr = await getAllBranchsMoneybyMonthsInYear("depreciation", year);
         monthsMaintenanceArr = await getAllBranchsMoneybyMonthsInYear("maintenance", year);
    }else if(type == 1){
        let id = document.getElementById("branch").value;
        monthsRevenueArr = await getBranchMoneybyMonthsInYear(id,"revenue", year);
        monthsDepreciationArr = await getBranchMoneybyMonthsInYear(id,"depreciation", year);
         monthsMaintenanceArr = await getBranchMoneybyMonthsInYear(id, "maintenance", year);
    }else if(type == 2){
        let id = document.getElementById("dorm").value;
        monthsRevenueArr = await getDormMoneybyMonthsInYear(id,"revenue", year);
        monthsDepreciationArr = await getDormMoneybyMonthsInYear(id,"depreciation", year);
         monthsMaintenanceArr = await getDormMoneybyMonthsInYear(id, "maintenance", year);
    }
    else if(type == 3){
        let id = document.getElementById("room").value;
        monthsRevenueArr = await getRoomMoneybyMonthsInYear(id,"revenue", year);
        monthsDepreciationArr = await getRoomMoneybyMonthsInYear(id,"depreciation", year);
         monthsMaintenanceArr = await getRoomMoneybyMonthsInYear(id, "maintenance", year);
    }
    else if(type == 4){
        let id = document.getElementById("slot").value;
        monthsRevenueArr = await getSlotMoneybyMonthsInYear(id,"revenue", year);
        monthsDepreciationArr = await getSlotMoneybyMonthsInYear(id,"depreciation", year);
         monthsMaintenanceArr = await getSlotMoneybyMonthsInYear(id, "maintenance", year);
    }
    console.log("monthsRevenueArr: " + monthsRevenueArr);
    console.log("monthsDepreciationArr: " + monthsDepreciationArr);
    console.log("monthsMaintenanceArr: " + monthsMaintenanceArr);
    const monthsExpensiveArr = [];
    for (let index = 0; index < monthsDepreciationArr.length; index++) {
        // console.log(monthsRevenueArr[index]);
        monthsExpensiveArr[index] = monthsDepreciationArr[index] + monthsMaintenanceArr[index];
    }

    const yearBarData = {
        labels: yearLabels,
        datasets: [
            {
                label: 'Revenue',
                backgroundColor: '#367E18',
                borderColor: '#367E18',
                data: monthsRevenueArr,
            },
            {
                label: 'Expense',
                data: monthsExpensiveArr,
                backgroundColor: '#CC3636',
                borderColor: '#CC3636',
            }
        ]
    };

    const yearBarConfig = {
        type: 'bar',
        data: yearBarData,
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
    

    const yearBarChart = new Chart(
        document.getElementById('yearBarChart'),
        yearBarConfig
    );
    
    function clickHandler(evt) {
        const points = yearBarChart.getElementsAtEventForMode(evt, 'nearest', { intersect: true }, true);

        if (points.length) {
            const firstPoint = points[0];
            const month = yearBarChart.data.labels[firstPoint.index];
            genExpensePieChart(month, year,monthsDepreciationArr[month - 1], monthsMaintenanceArr[month - 1]);
        }
        
    }

    document.getElementById('yearBarChart').onclick = clickHandler;
}

function genExpensePieChart(month, year, depreciation, maintenance){
    var months = [ "January", "February", "March", "April", "May", "June", 
           "July", "August", "September", "October", "November", "December" ];
    chartCanvas = document.getElementById('yearPieChart');
    chart = Chart.getChart(chartCanvas);
    if (chart != undefined) {
        chart.destroy();
    }
    const yearPieData = {
        labels: [
            'Depreciation',
            'Maintenance',
            "Other"
          ],
          datasets: [{
            data: [depreciation, maintenance, 0],
            backgroundColor: [
              '#F57328',
              '#FFE9A0',
              '#E7B10A'
            ],
            hoverOffset: 4
          }]
    };

    const yearPieConfig = {
        type: 'pie',
        data: yearPieData,
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
                    text: `Expense in ${months[month - 1]} ${year}`
                }
            }
        }
    };
    const yearPieChart = new Chart(
        document.getElementById('yearPieChart'),
        yearPieConfig
    );
}

const currDate = new Date();
const currYear = currDate.getFullYear();
const currMonth = currDate.getMonth();
// const MonthInput = document.getElementById("month");
// if (currMonth >= 10) {
//     MonthInput.value = currYear + "-" + currMonth;
// } else {
//     MonthInput.value = currYear + "-0" + currMonth;
// }
document.getElementById("year").value =currYear;
// document.getElementById("month").value = `${currDate.getFullYear()}-${currDate.getMonth()}`;
// console.log(document.getElementById("month").value)
genLineChart();

function showBy(){
    let idSelected = document.getElementById("show-by").value
    let removeDropDown = document.getElementById("load")
    let reMoveRequest = ``
    if(idSelected == 0){
        removeDropDown.innerHTML = ""
    }
    if (idSelected ==1) {
        reMoveRequest = `Branch: <br><select class="SBB-input" id="branch" onchange="">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `
               removeDropDown.innerHTML = reMoveRequest
               loadbranch();
    } if (idSelected == 2 ) {
    reMoveRequest = `Branch: <br><select class="SBB-input" id="branch" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  Dorm: <br><select class="SBB-input" id="dorm" onchange="">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       `
  removeDropDown.innerHTML = reMoveRequest
  loadbranch();
    }if (idSelected == 3) {
        reMoveRequest = `Branch: <br><select class="SBB-input" id="branch" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  Dorm: <br><select class="SBB-input" id="dorm" onchange="loadrooms()">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       Room: <br><select class="SBB-input" id="room" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
      `
  removeDropDown.innerHTML = reMoveRequest
  loadbranch();
    }if (idSelected == 4) {
        reMoveRequest = `Branch: <br><select class="SBB-input" id="branch" onchange=" loaddorm()">
        <option value="" disabled selected>Chọn một lựa chọn</option>
      </select><br> 
      Dorm: <br><select class="SBB-input" id="dorm" onchange="loadrooms()">
           <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           Room: <br><select class="SBB-input" id="room" onchange=" loadslots()">
             <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           Slot: <br><select class="SBB-input" id="slot" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br></br>`
      removeDropDown.innerHTML = reMoveRequest
      loadbranch();
    }
}

function loadbranch() {
    let branchDropDown = document.getElementById("branch");
    console.log(1);
    console.log(branchDropDown.value);
    let url = "http://localhost:8081/api/v1/branchs";
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                branchDropDown.appendChild(option);
            });

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loaddorm() {
    let dormDropDown = document.getElementById("dorm");
    dormDropDown.innerHTML = '';
    const selectElement = document.getElementById("branch");
    const branchId = selectElement.value;
    console.log("branchid: " + branchId);
    let url = "http://localhost:8081/api/v1/dorms/branch/" + branchId;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                dormDropDown.append(option);
            });

        })
        .then(
            loadrooms
        )

        .catch(error => {
            console.error('Error:', error);
        });
}


function loadrooms() {
    let roomDropDown = document.getElementById("room");
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dorm");
    const dormId = selectElement.value;
    console.log("dormid: " + dormId);
    let url = "http://localhost:8081/api/v1/rooms/dorm/" + dormId;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                roomDropDown.append(option);
            });

        }

        )
        .then(
            loadslots
        )
        .catch(error => {
            console.error('Error:', error);
        });
}


function loadslots() {
    let slotDropDown = document.getElementById("slot");
    slotDropDown.innerHTML = '';
    const selectElement = document.getElementById("room");
    const roomId = selectElement.value;
    console.log("roomid for slot: " + roomId);
    let url = "http://localhost:8081/api/v1/slots/room/" + roomId ;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                slotDropDown.append(option);
            });

        })
        .catch(error => {
            console.log("error");
        });
}



