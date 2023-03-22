document.addEventListener("DOMContentLoaded", function () {
    loadBookingSchedule()
    checkJwtExpiration(localStorage.getItem("jwt"))
    loadbranch()
});
setInterval(function () {
    checkJwtExpiration(localStorage.getItem("jwt"));
}, 10000);
function checkJwtExpiration(token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = decodedToken.exp * 1000; // convert to milliseconds

    // Check if the token has expired
    if (Date.now() >= expirationTime) {
        alert('Token has expired');
        console.log('Token has expired.\n Please login again!');
        window.location.href = "login.html";
    } else {
        console.log('Token is still valid');
    }
}

function loadForm() {
    let form = document.getElementById("form")
    let formSchedule = `
    
    keepStartDate: <br><input class="SBB-input" type="datetime-local" id="keepStartDate">
    <br>
    keepEndDate: <br><input class="SBB-input" type="datetime-local" id="keepEndDate">
    <br>
    NewStartDate: <br><input class="SBB-input" type="datetime-local" id="newStartDate">
    <br>
    NewEndDate: <br><input class="SBB-input" type="datetime-local" id="newEndDate">
    <br>
    StartDate: <br><input class="SBB-input" type="datetime-local" id="startDate">
    <br>
    EndDate: <br><input class="SBB-input" type="datetime-local" id="endDate">
    <br><br>
    <button class="orange-btn1" type="" id="updateButton" onclick="checkDate()">Add</button><br><br>
    `
    form.innerHTML = formSchedule

}

function loadbranch() {
    let branchDropDown = document.getElementById("branchs");
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

function loadBookingSchedule() {

    let url = "http://localhost:8081/api/v1/booking-schedule";
    let bookingScheduleTable = document.getElementById("bookingSchedule");
    let bookingSchedule = ``;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })

        .then(response => response.json())

        .then(dataJson => {

            dataJson.data.forEach(element => {
                bookingSchedule += `<tr>
                <td>${element.id}</td>
                <td>${element.branch.name}</td>
                <td>${element.keepStartDate}</td>
                <td>${element.keepEndDate}</td>
                <td>${element.newStartDate}</td>
                <td>${element.newEndDate}</td>
                <td>${element.startDate}</td>
                <td>${element.endDate}</td>
                <td style=" font-weight:900; font-size:18px">${element.reset}</td>
                <td style="color: green; font-weight:900; font-size:18px">${element.active ? "Actived" : `<button class="btn btn-primary"   onclick="activeSchedule(${element.branch.id})" >Active</button>`}</td>
                </tr>`;
                console.log(dataJson);
            });

            bookingScheduleTable.innerHTML = bookingSchedule;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function activeSchedule(branchid) {
    let url = "http://localhost:8081/api/v1/booking-schedule/reset-slots";

    jsonData = { id: branchid };
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then (console.log(1))
        .then(loadBookingSchedule)
        .catch(error => {
            console.error('Error:', error);
        });
        
}

function loadUpdateForm(id) {
    let updateForm = document.getElementById("update")
    let updateSchedule = `
    keepStartDate: <br><input class="SBB-input" type="datetime-local" id="keepStartDate">
    <br>
    keepEndDate: <br><input class="SBB-input" type="datetime-local" id="keepEndDate">
    <br>
    NewStartDate: <br><input class="SBB-input" type="datetime-local" id="newStartDate">
    <br>
    NewEndDate: <br><input class="SBB-input" type="datetime-local" id="newEndDate">
    <br>
    StartDate: <br><input class="SBB-input" type="datetime-local" id="startDate">
    <br>
    EndDate: <br><input class="SBB-input" type="datetime-local" id="endDate">
    <br>
    isReset: <br><input class="SBB-input type="text" id="isReset">
    <br>`
    updateForm.innerHTML = updateSchedule
    loadBookingScheduleById(id)
}

function checkDate() {
    let keepStartDate = document.getElementById("keepStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let keepEndDate = document.getElementById("keepEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewStartDate = document.getElementById("newStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewEndDate = document.getElementById("newEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let StartDate = document.getElementById("startDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let EndDate = document.getElementById("endDate").value.slice(0, 19).replace('T', ' ') + ":00";

    if (keepEndDate > NewStartDate) {
        alert("keepEndDate must be earlier than NewStartDate!!")
    }else if(keepEndDate<keepStartDate){
        alert("keepStartDate must be earlier than keepEndDate!!")
    }else if (NewEndDate < NewStartDate) {
        alert("NewStartDate must be earlier than NewEndDate!!")
    }else if (StartDate > EndDate) {
        alert("StartDate must be earlier than EndDate!!")
    }else if (StartDate <= keepEndDate) {
        alert("keepEndDate must be earlier than StartDate!!")
    }else if (StartDate <= NewEndDate) {
        alert("NewEndDate must be earlier than StartDate!!")
    }
    else{
        addBookingSchedule();
    }
}

function addBookingSchedule() {
    let url = "http://localhost:8081/api/v1/booking-schedule";
    let branch = document.getElementById("branchs").value
    let keepStartDate = document.getElementById("keepStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let keepEndDate = document.getElementById("keepEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewStartDate = document.getElementById("newStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewEndDate = document.getElementById("newEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let StartDate = document.getElementById("startDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let EndDate = document.getElementById("endDate").value.slice(0, 19).replace('T', ' ') + ":00";
    jsonData = { branch: { id: branch }, keepStartDate: keepStartDate, keepEndDate: keepEndDate, newStartDate: NewStartDate, newEndDate: NewEndDate, startDate: StartDate, endDate: EndDate};
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {

            console.log(data);
        })
        .then(loadBookingSchedule)
        .catch(error => {
            console.error('Error:', error);
        });
}
function loadBookingScheduleById(id) {
    let url = "http://localhost:8081/api/v1/booking-schedule/" + id;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            let bookingScheduleSelected = dataJson.data
            document.getElementById("keepStartDate").value = bookingScheduleSelected.keepStartDate;
            document.getElementById("keepEndDate").value = bookingScheduleSelected.keepEndDate;
            document.getElementById("newStartDate").value = bookingScheduleSelected.newStartDate;
            document.getElementById("newEndDate").value = bookingScheduleSelected.newEndDate;
            document.getElementById("startDate").value = bookingScheduleSelected.startDate;
            document.getElementById("endDate").value = bookingScheduleSelected.endDate;
            document.getElementById("isReset").value = bookingScheduleSelected.reset;

            updateBTN = document.getElementById("updateButton");


            updateBTN.onclick = function () {
                updateBookingSchedule(id)
            };
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateBookingSchedule(id) {
    url = "http://localhost:8081/api/v1/booking-schedule/" + id
    let keepStartDate = document.getElementById("keepStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let keepEndDate = document.getElementById("keepEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewStartDate = document.getElementById("newStartDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let NewEndDate = document.getElementById("newEndDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let StartDate = document.getElementById("startDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let EndDate = document.getElementById("endDate").value.slice(0, 19).replace('T', ' ') + ":00";
    let isReset = document.getElementById("isReset").value
    jsonData = { keepStartDate: keepStartDate, keepEndDate: keepEndDate, newStartDate: NewStartDate, newEndDate: NewEndDate, startDate: StartDate, endDate: EndDate, reset: isReset };
    console.log(jsonData);
    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .then(loadBookingSchedule)
        .catch(error => {
            console.error('Error:', error);
        });

}

