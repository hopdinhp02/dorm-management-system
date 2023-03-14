loadbranch()
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
    console.log(url);
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
    url = "http://localhost:8081/api/v1/elec-water-usages/" + roomID + "/record-electric-water-usage";
    const monthInput = document.getElementById("month").value;
    // Tách chuỗi giá trị thành năm và tháng
    const [year, month] = monthInput.split("-");
    // Tạo đối tượng Date với năm và tháng đã lấy được, và đặt ngày là 0 để lấy ngày cuối cùng trong tháng
    const lastDayOfMonth = new Date(year, month, 0);
    console.log(lastDayOfMonth);
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    const formattedDate = lastDayOfMonth.toLocaleDateString('en-CA', options).replace(/\//g, '/');
    let electricStart = document.getElementById("electricStart").value
    let electricEnd = document.getElementById("electricStart").value
    let waterStart = document.getElementById("waterStart").value
    let waterEnd = document.getElementById("waterEnd").value


    jsonData = {electricStart: electricStart, electricEnd: electricEnd, waterStart: waterStart, waterEnd:waterEnd, monthPay:formattedDate, room : {id: roomID} };
    console.log(jsonData);
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
        })
        .catch(error => {
            console.error('Error:', error);
        });
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

}
function accept(id, value) {

    if (confirm("The value has changed to: " + value)) {
        updateBookingRequest(id, value)
    } else {
        setTimeout(loadBooking(), 500)


    }
}