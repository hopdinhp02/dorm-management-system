document.addEventListener("DOMContentLoaded", function () {
  checkJwtExpiration(localStorage.getItem("jwt"))
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
            loadbranch();
          }

      })
   
      .catch(error => {
          console.error('Error:', error);
      });
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

function checkDayKeep() {
  let url = "http://localhost:8081/api/v1/booking-schedule/check-day-keep";
  let text = document.getElementById("check");
  let bookingRequest = ``;
  let branchId = document.getElementById("branchs").value
  let dorm = document.getElementById("dorm")
  let dormRequest = ``;

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
        bookingRequest = `<h1>Keeping Day</h1>`
        text.innerHTML = bookingRequest


        checkliving();


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
  let url = "http://localhost:8081/api/v1/booking-schedule/check-day-booking";
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
        bookingRequest = `<h1>Booking Day</h1>`
        text.innerHTML = bookingRequest
        dormRequest = `
        <div id="booking-request-form">
              <div class="col-xs-12 col-md-7 no-padding no-margin">
                <h1 id="check"></h1>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                  <div class="SBB-available-btn">
                    <a href="#" class="sidebar-linkItem flex items-center">See available beds</a>
                  </div>
                  <div class="flex-1">
                    <label class="SBB-input-label no-margin">Room Type</label>
                    <div class="" style="width: 100%;">
                      <input class="SBB-input " readonly="" type="text" value="SVVN - 3 beds - 950.000">
                      <input id="RoomTypeId" name="RoomTypeId" type="hidden" value="3">
                    </div>
                  </div>
                </div>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                  <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin">Dom</label>
                    <div class="my-select-style">
                      <select class="SBB-input" id="dorms" name="DomId" onchange="loadrooms()"></select>
                    </div>
                  </div>
                  <div class="flex-1">
                    <label class="SBB-input-label no-margin">Room</label>
                    <div class="my-select-style">
                      <select class="SBB-input" id="rooms" name="Rooms"></select>
                    </div>
                  </div>
                </div>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                <div class="flex-1">
                  <label class="SBB-input-label no-margin" for="Semester">Slot</label>
                  <div class="my-select-style">
                    <select class="SBB-input" id="slots" name="Slot">
                    </select>
                  </div>
                </div>
                <div class="SBB-layout-1">
                  <label class="SBB-input-label no-margin" for="Note">Note</label>
                  <div class="">
                    <input class="SBB-input text-box single-line" id="Note" name="Note" type="text" value="" />
                  </div>
                </div>
                </div>
                  <button class="btn btn-primary"  onclick="addBookingRequests()">Add</button>
        </div>`          

        dorm.innerHTML = dormRequest;
        loaddorm();
      } else {
        bookingRequest = `<h1>Sorry, it's not right time to book a new bed!</h1>`
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
  let branchId = document.getElementById("branchs").value
  let url = "http://localhost:8081/api/v1/booking-requests/check-living/branchs/" + branchId ;
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
      } else {
        bookingRequest = `<h1>keeping day: 
                                          you don't have room</h1>`
        text.innerHTML = bookingRequest
        document.getElementById("dorm").innerHTML = "";
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
      if (data.room.dorm.branch.id == branchId) {
        dormRequest = `
            <div id="booking-request-form">
              <form>
                    <div class="col-xs-12 col-md-7 no-padding no-margin">
                <h1 id="check"></h1>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                  <div class="SBB-available-btn">
                    <a href="#" class="sidebar-linkItem flex items-center">See available beds</a>
                  </div>
                  <div class="flex-1">
                    <label class="SBB-input-label no-margin">Room Type</label>
                    <div class="" style="width: 100%;">
                      <input class="SBB-input " readonly="" type="text" value="SVVN - 3 beds - 950.000">
                      <input id="RoomTypeId" name="RoomTypeId" type="hidden" value="3">
                    </div>
                  </div>
                </div>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                  <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin">Dom</label>
                    <div class="my-select-style">
                      <select class="SBB-input" id="dorms" name="DomId" onchange="loadrooms()">
                      <option value="${data.room.dorm.id}" disabled selected>${data.room.dorm.name}</option></select></select>
                    </div>
                  </div>
                  <div class="flex-1">
                    <label class="SBB-input-label no-margin">Room</label>
                    <div class="my-select-style">
                      <select class="SBB-input" id="rooms" name="Rooms">
                      <option value="${data.room.id}" disabled selected>${data.room.name}</option></select>
                    </div>
                  </div>
                </div>
                <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                <div class="flex-1">
                  <label class="SBB-input-label no-margin" for="Semester">Slot</label>
                  <div class="my-select-style">
                    <select class="SBB-input" id="slots" name="Slot">
                    <option value="${data.id}" disabled selected>${data.name}</option>
                    </select>
                  </div>
                </div>
                <div class="SBB-layout-1">
                  <label class="SBB-input-label no-margin" for="Note">Note</label>
                  <div class="">
                    <input class="SBB-input text-box single-line" id="Note" name="Note" type="text" value="" />
                  </div>
                </div>
              </div>
                  <button class="btn btn-primary"  onclick="addBookingRequests()">Add</button>
                  </form>
            </div>`
        //     Dorm: <br><select class="SBB-input" id="dorms" onchange="">
        //     <option value="${data.room.dorm.id}" disabled selected>${data.room.dorm.name}</option>
        //   </select>
        //   <br>
        //   Room:<br> <select class="SBB-input" id="rooms" onchange="">
        //          <option value="${data.room.id}" disabled selected>${data.room.name}</option>
        //   </select>
        //   <br>
        //   slot: <br><select class="SBB-input" id="slots">
        //   <option value="${data.id}" disabled selected>${data.name}</option>
        //   </select><br>
        //   Note: <input type="text" id="Note"><br>
        //   <button class="btn btn-primary"  onclick="addBookingRequests()">Add</button>`

        dorm.innerHTML = dormRequest;
      } else {

      }

    })
    .catch(error => {
      console.error('Error:', error);
    });
}
// const handleToggleSidebar = ()=>{
//     if (isSidebarFull) {
//         sidebarFullTag.classList.add("hidden");
//         isSidebarFull = false;
//         contentBodyTag.classList.remove("pl-328");
//     } else {
//         sidebarFullTag.classList.remove("hidden");
//         isSidebarFull = true;
//         contentBodyTag.classList.add("pl-328");
//     }
// };

// $(document).ready(function(){
//     $("#sidebarCollapse").on('click', function(){
//         $("#sidebar").toggleClass('active');
//     });
// });