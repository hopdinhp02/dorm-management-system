
document.addEventListener("DOMContentLoaded", function () {
    loadBooking()
    checkUserIsBook()
    checkJwtExpiration(localStorage.getItem("jwt"))
    
});
setInterval(function(){
    checkJwtExpiration(localStorage.getItem("jwt"));
}, 10000); 
function checkJwtExpiration(token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = decodedToken.exp * 1000; // convert to milliseconds
  
    // Check if the token has expired
    if (Date.now() >= expirationTime) {
        alert('Token has expired');
      console.log('Token has expired.\n Please login again!');
      window.location.href="login.html";
    } else {
      console.log('Token is still valid');
    }
  }
function checkUserIsBook() {
    let url = "http://localhost:8081/api/v1/booking-requests/userInfo/is-booked";
    let bookingForm = document.getElementById("booking-request-form");
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
                bookingForm.innerHTML = "<h3  style='color: red;'>You have booked</h3>";
            } else {
                bookingForm.innerHTML = `<form>
 
                Branch: <select id="branchs" onchange="loaddorm()">
                  <option value="" disabled selected>Chọn một lựa chọn</option>
                </select><br>
                Dorm: <select id="dorms" onchange="loadrooms()">
                  <option value="" disabled selected>Chọn một lựa chọn</option>
            
                </select>
                <br>
                Room: <select id="rooms" onchange="loadbeds()"></select>
                <br>
                Bed: <select id="beds"></select><br>
                Note: <input type="text" id="Note"><br>
              </form><br>
              <button class="btn btn-primary" type="submit" onclick="addBookingRequests()">Add</button>`;
              loadbranch();
            }

        })
     
        .catch(error => {
            console.error('Error:', error);
        });
}
function loadBooking() {
    let url = "http://localhost:8081/api/v1/booking-requests";
    let bookingTable = document.getElementById("booking");
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
            console.log(dataJson);
            dataJson.data.forEach(element => {
                bookingRequest += `<tr>
                <td>${element.id}</td>
                <td>${element.bed.id}</td>
                <td>${element.userInfo.id}</td>
                <td>${element.note}</td>
                <td>${element.startDate}</td>
                <td>${element.endDate}</td>
                <td>${element.createdDate}</td>
                <td>
                <select onchange=" accept(${element.id}, this.value)">
                     <option value="Processing" ${element.status == "Processing" ? "selected" : ""}>Processing</option>
                     <option value="Paying"  ${element.status == "Paying" ? "selected" : ""}>Paying</option>
                     <option value="Accept"  ${element.status == "Accept" ? "selected" : ""}>Accept</option>
                     <option value="Decline"  ${element.status == "Decline" ? "selected" : ""}>Decline</option>
                </select>
                </td>
                </tr>`;
                console.log(bookingRequest)
            });

            bookingTable.innerHTML = bookingRequest;

        })
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


function updateBookingRequest(id, value) {
    url = "http://localhost:8081/api/v1/booking-requests/" + id
    let bookingRequestStatus = value
    jsonData = { status: bookingRequestStatus };
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
        .then(checkUserIsBook)
        .catch(error => {
            console.error('Error:', error);
        });
    setTimeout(loadBooking, 500);
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
            loadbeds
        )
        .catch(error => {
            console.error('Error:', error);
        });
}


function loadbeds() {
    let bedDropDown = document.getElementById("beds");
    bedDropDown.innerHTML = '';
    const selectElement = document.getElementById("rooms");
    const roomId = selectElement.value;
    console.log("roomid for bed: " + roomId);
    let url = "http://localhost:8081/api/v1/beds/room/" + roomId + "/available";
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
                bedDropDown.append(option);
            });

        })
        .catch(error => {
            console.log("error");
        });
}


function addBookingRequests() {
    url = "http://localhost:8081/api/v1/booking-requests";
    BedID = document.getElementById("beds").value;
    Note = document.getElementById("Note").value;
    jsonData = { bed: { id: BedID }, note: Note };
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
        .then(loadBooking)
        .catch(error => {
            console.error('Error:', error);
        });
}