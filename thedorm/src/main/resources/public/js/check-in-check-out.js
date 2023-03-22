

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

function load() {
    let form = document.getElementById("checkIn-Out-form")
    let formFacility = ``

    formFacility = `<form>
    Branch: <br><select class="SBB-input" id="branchs" onchange="loaddorm()">
      <option value="" disabled selected>Chọn một lựa chọn</option>
    </select><br>
    Dorm: <br><select class="SBB-input" id="dorms" onchange="loadrooms()">
      <option value="" disabled selected>Chọn một lựa chọn</option>
    </select>
    <br>
    Room:<br> <select class="SBB-input" id="rooms" onchange="loadslots()"></select>
    <br>
    slot: <br><select class="SBB-input" id="slots" onchange="">
      <div id="op"></div>
    </select><br>
  </form><br>`

    form.innerHTML = formFacility;
    loadbranch();
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
    let url = "http://localhost:8081/api/v1/slots/room/" + roomId;
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

function checkInCheckOut() {
    let selected = document.getElementById("checkInCheckOut").value
    console.log(selected);
    if (selected == 1) {
        checkIn()
    } else if (selected == 2) {
        checkOut()
    }
}

function checkIn() {
    let slotID = document.getElementById("slots").value
    console.log(slotID);
    let url = "http://localhost:8081/api/v1/resident-histories/guard/check-in/slots/" + slotID;
    let checkInOutTable = document.getElementById("checkInCheckOut2")
    let checkInOut = ``
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
            dataJson.data.forEach(element => {
                checkInOut = `<tr>
                <td scope="row">${element.userInfo.id}</td>
                <td><img src="/images/avata.jpg" style="width: 80px; height: 80px;" onclick="enlargeImage(this)" id="myImage"></td>
                <td><img src="/images/cccd.jpg" style="width: 150px; height: 80px;" onclick="enlargeImage(this)" id="myImage"></td>
                <td>${element.userInfo.name}</td>
                <td>${element.userInfo.email}</td>
                <td>${element.userInfo.phone}</td>
                <td><button class="btn btn-primary" type="" onclick="acceptCheckIn(${element.userInfo.id})">Check In</button></td>
                </tr>
                `;
            });
            console.log(checkInOut);
            checkInOutTable.innerHTML = checkInOut;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function enlargeImage(img) {
    var overlay = document.createElement("div");
    overlay.setAttribute("id", "overlay");
    overlay.setAttribute("onclick", "shrinkImage()");
    overlay.setAttribute("class", "floating-div");
    document.body.appendChild(overlay);
    
    var image = document.createElement("img");
    image.setAttribute("src", img.src);
    image.setAttribute("id", "enlarged-image");
    console.log(image);
    overlay.appendChild(image)
    console.log(overlay);
    
  }
  
  function shrinkImage() {
    var overlay = document.getElementById("overlay");
    var image = document.getElementById("enlarged-image");
    document.body.removeChild(overlay);
    document.body.removeChild(image);
  }


function acceptCheckIn(id) {
    url = "http://localhost:8081/api/v1/resident-histories/guard/check-in";
    jsonData = { id: id };
    fetch(url,
        {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("jwt")}`
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(checkIn)
        .then(alert("Checkin successfully!"))
        .then(checkInCheckOut)
        .catch(error => {
            console.error('Error:', error);
        });


}

function checkOut() {
    let slotID = document.getElementById("slots").value
    let url = "http://localhost:8081/api/v1/resident-histories/guard/check-out/slots/" + slotID;
    let checkInOutTable = document.getElementById("checkInCheckOut2")
    let checkInOut = ``
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
            dataJson.data.forEach(element => {
                checkInOut = `<tr>
                <td scope="row">${element.userInfo.id}</td>
                <td><img src="/images/avata.jpg" style="width: 80px; height: 80px;"></td>
                <td>${element.userInfo.name}</td>
                <td>${element.userInfo.email}</td>
                <td>${element.userInfo.phone}</td>
                <td><button class="btn btn-primary" type="" onclick="acceptCheckOut(${element.userInfo.id})">Check Out</button></td>
                </tr>
                `;
            });
            console.log(checkInOut);
            checkInOutTable.innerHTML = checkInOut;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function acceptCheckOut(id) {
    url = "http://localhost:8081/api/v1/resident-histories/guard/check-out";
    jsonData = { id: id };
    fetch(url,
        {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("jwt")}`
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(checkOut)
        .then(alert("Checkout successfully!"))
        .then(checkInCheckOut)
        .catch(error => {
            console.error('Error:', error);
        });


}

