document.addEventListener("DOMContentLoaded", function () {
    checkJwtExpiration(localStorage.getItem("jwt"))
    loadbranch()
    checkUserIsBook()
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



function loadbranch() {
    let branchDropDown = document.getElementById("branchs");
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
        .then(
            loadrooms
        )

        .catch(error => {
            console.error('Error:', error);
        });
}


function loadrooms() {
    let roomDropDown = document.getElementById("rooms");
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dorms");
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
    let slotDropDown = document.getElementById("slots");
    slotDropDown.innerHTML = '';
    const selectElement = document.getElementById("rooms");
    const roomId = selectElement.value;
    console.log("roomid for slot: " + roomId);
    let url = "http://localhost:8081/api/v1/slots/room/" + roomId + "/available";
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

function checkUserIsBook() {
    let url = "http://localhost:8081/api/v1/booking-requests/userInfo/is-booked";
    let text = document.getElementById("check");
    let bookingForm = ``;
    
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(dataJson => {
            console.log(dataJson);
            if (dataJson.data == true) {
                bookingForm = "<h3  style='color: red;'>You have booked</h3>";
                text.innerHTML = bookingRequest

            } 

        })
     
        .catch(error => {
            console.error('Error:', error);
        });
}


function checkDayKeep() {
    let url = "http://localhost:8081/api/v1/booking-requests/check-day-keep";
    let text = document.getElementById("check");
    let bookingRequest = ``;
    let branchId = document.getElementById("branchs").value
    let dormRequest = ``;
    let dorm = document.getElementById("dorm")

    jsonData = { id: branchId };
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(dataJson => {
            console.log(dataJson);
            if (dataJson.data == true) {
                bookingRequest = `<h1>Keeping Date</h1>`
                text.innerHTML = bookingRequest
                checkliving();
                dormRequest =``;
                dorm.innerHTML = dormRequest; 
                 

            } else {
                checkDayBooking()

            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function addBookingRequests() {
    url = "http://localhost:8081/api/v1/booking-requests";
    slotID = document.getElementById("slots").value;
    Note = document.getElementById("Note").value;
    jsonData = { slot: { id: slotID }, note: Note };
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

function checkDayBooking() {
    let url = "http://localhost:8081/api/v1/booking-requests/check-day-booking";
    let text = document.getElementById("check");
    let bookingRequest = ``;
    let branchId = document.getElementById("branchs").value
    let dormRequest = ``;
    let dorm = document.getElementById("dorm")
    jsonData = { id: branchId };
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)

    })
        .then(response => response.json())
        .then(dataJson => {
            if (dataJson.data == true) {
                bookingRequest = `<h1>Booking Date</h1>`
                text.innerHTML = bookingRequest
                dormRequest = `Dorm: <br><select class="SBB-input" id="dorms" onchange="loadrooms()">
                <option value="" disabled selected>Chọn một lựa chọn</option>
              </select>
              <br>
              Room:<br> <select class="SBB-input" id="rooms" onchange="loadslots()"></select>
              <br>
              slot: <br><select class="SBB-input" id="slots"></select><br>
              Note: <input type="text" id="Note"><br>
                  <button class="btn btn-primary"  onclick="addBookingRequests()">Add</button>`
                  
                dorm.innerHTML = dormRequest; 
                loaddorm();
            } else {
                bookingRequest = `<h1>You can't BookRoom</h1>`
                text.innerHTML = bookingRequest
                dormRequest = ``;
                dorm.innerHTML = dormRequest; 

            }

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function checkliving() {
    let url = "http://localhost:8081/api/v1/booking-requests/check-living";
    let text = document.getElementById("check");
    let bookingRequest = ``;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }


    })
        .then(response => response.json())
        .then(dataJson => {
            if (dataJson.data == true) {
                getOldSlot();
            }else{
                bookingRequest = `<h1>You don't have room</h1>`
                text.innerHTML = bookingRequest
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function getOldSlot() {
    let url = "http://localhost:8081/api/v1/booking-requests/get-old-slot";
    let branchId = document.getElementById("branchs").value
    let dormRequest = ``;
    let dorm = document.getElementById("dorm")
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },


    })
        .then(response => response.json())
        .then(dataJson => {
            console.log(dataJson);
            data = dataJson.data 
            console.log(data.room.dorm.branch.id == branchId);
console.log(branchId);
console.log(data.room.dorm.branch.id);
                if (data.room.dorm.branch.id == branchId) {
                    dormRequest = `Dorm: <br><select class="SBB-input" id="dorms" onchange="">
                    <option value="${data.room.dorm.id}" disabled selected>${data.room.dorm.name}</option>
                  </select>
                  <br>
                  Room:<br> <select class="SBB-input" id="rooms" onchange="">
                         <option value="${data.room.id}" disabled selected>${data.room.name}</option>
                  </select>
                  <br>
                  slot: <br><select class="SBB-input" id="slots">
                  <option value="${data.id}" disabled selected>${data.name}</option>
                  </select><br>
                  Note: <input type="text" id="Note"><br>
                  <button class="btn btn-primary"  onclick="addBookingRequests()">Add</button>`
                  
                dorm.innerHTML = dormRequest; 
                } else {
                    
                }
                
        })
        .catch(error => {
            console.error('Error:', error);
        });
}