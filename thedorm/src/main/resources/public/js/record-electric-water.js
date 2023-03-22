loadbranch()

// let month = document.getElementById("month")
// let x = new Date();
// let lastMonth = x.getFullYear() + "-" + x.getMonth()
// console.log(x);
// console.log(lastMonth);
// console.log(month.value);

const currDate = new Date();
const currYear = currDate.getFullYear();
const currMonth = currDate.getMonth();
if (currMonth >= 10) {
    month.value = currYear + "-" + currMonth;
} else {
    month.value = currYear + "-0" + currMonth;
}



function loadbranch() {
    let branchDropDown = document.getElementById("branchs");
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


function disabledForm(){
    document.getElementById("recordElectric").innerHTML = "";
}
function loaddorm() {
    

    let dormDropDown = document.getElementById("dorms");
    dormDropDown.innerHTML = '';
    const selectElement = document.getElementById("branchs");
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


        .catch(error => {
            console.error('Error:', error);
        });
}

function loadRoom() {

    let roomDropDown = document.getElementById("rooms")
    roomDropDown.innerHTML = ``
    date = document.getElementById("month");
    const monthYear = date.value.split('-');
    const year = monthYear[0];
    const month = monthYear[1];
    
    let id = document.getElementById("dorms").value
    let url = "http://localhost:8081/api/v1/elec-water-usages/all-rooms-not-even-record-electric-water-usage-of-dorm/" + id + "?month=" + month + "&year=" + year;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            var option = document.createElement("option");
                option.text = "Chọn một lựa chọn"
                option.value = ""
                option.disabled = true
                option.selected = true
                roomDropDown.append(option);
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                roomDropDown.append(option);
            });

        }

        )
        .catch(error => {
            console.error('Error:', error);
        });
}

function recordElectric() {
    let roomID = document.getElementById("rooms").value
    console.log(2);
    url = "http://localhost:8081/api/v1/elec-water-usages/" + roomID + "/write-electric-water-usage";
    const monthInput = document.getElementById("month").value;
    // Tách chuỗi giá trị thành năm và tháng
    const [year, month] = monthInput.split("-");
    // Tạo đối tượng Date với năm và tháng đã lấy được, và đặt ngày là 0 để lấy ngày cuối cùng trong tháng
    const lastDayOfMonth = new Date(year, month, 0);
    console.log(lastDayOfMonth);
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    const formattedDate = lastDayOfMonth.toLocaleDateString('en-CA', options).replace(/\//g, '/');
    let electricStart = document.getElementById("electricStart").value
    let electricEnd = document.getElementById("electricEnd").value
    let waterStart = document.getElementById("waterStart").value
    let waterEnd = document.getElementById("waterEnd").value
    
    
    let electricStartInt = parseInt(electricStart);
    let electricEndInt = parseInt(electricEnd);
    let waterStartInt = parseInt(waterStart);
    let waterEndInt = parseInt(waterEnd);
    console.log(electricEnd);
    if ( electricStartInt > electricEndInt) {
        console.log(1);
        alert("electricEnd must be greater than electricStart")
    }
    else if (waterStartInt > waterEndInt) {
        alert("WaterEnd must be greater than waterStart")
        
    }
    else{
        jsonData = {electricStart: electricStart, electricEnd: electricEnd, waterStart: waterStart, waterEnd:waterEnd, monthPay:formattedDate, room : {id: roomID} };
        fetch(url,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${localStorage.getItem("jwt")}`
                },
    
                body: JSON.stringify(jsonData)
            }
        )
            .then(respone => respone.json())
            .then(data => {
    
                console.log(data);
                alert("Record electric water usage successfully!");
                disabledForm();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    
}

function loadForm(){
    let form = document.getElementById("recordElectric")
    form.innerHTML = `
    <label class="SBB-input-label no-margin">ElectricStart</label><br>
          <input class="SBB-input" type="number" id="electricStart"><br>
    <label class="SBB-input-label no-margin">ElectricEnd</label><br>
          <input class="SBB-input" type="number" id="electricEnd"><br>
    <label class="SBB-input-label no-margin">WaterStart</label><br>      
          <input class="SBB-input" type="number" id="waterStart"><br>
    <label class="SBB-input-label no-margin">WaterEnd</label><br>
          <input class="SBB-input" type="number" id="waterEnd"><br><br>
    <button style="margin-left: 94%  ;" type="button" onclick="recordElectric()"  class="orange-btn">Record</button>`
    loadStartElectricWater();
}

function loadStartElectricWater(){
    let roomId = document.getElementById("rooms").value
    let url = "http://localhost:8081/api/v1/elec-water-usages/endNumberElectricWater"+`?roomid=${roomId}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            if (jsonData.data == "") {
                document.getElementById("electricStart").value = 0
            document.getElementById("waterStart").value = 0
            } else {
                document.getElementById("electricStart").value = jsonData.data.electricEnd
            document.getElementById("waterStart").value = jsonData.data.waterEnd
        }
            }
            

        )
        .catch(error => {
            console.error('Error:', error);
        });
}


function accept(id, value) {

    if (confirm("The value has changed to: " + value)) {
        updateBookingRequest(id, value)
    } else {
        setTimeout(loadBooking(), 500)


    }
}