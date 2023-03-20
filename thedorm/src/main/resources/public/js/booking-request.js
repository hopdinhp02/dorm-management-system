document.addEventListener("DOMContentLoaded", function () {
  checkJwtExpiration(localStorage.getItem("jwt"))
  checkUserIsBook()
  getBalance()
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
    window.location.href = "/login.html";
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
      if (dataJson.data != false) {
        bookingForm.innerHTML = `<h3 class="big-title"  style='color: red;'>You have booked</h3>
             <div class="col-xs-12 col-md-7 no-padding no-margin"
             style="font-size: 20px;" >
              Branch: ${dataJson.data.slot.room.dorm.branch.name}<br>
              Dorm: ${dataJson.data.slot.room.dorm.name}<br>
              Room: ${dataJson.data.slot.room.name}<br>
              Slot: ${dataJson.data.slot.name}<br>
              Start Date: ${dataJson.data.startDate}<br>
              End Date: ${dataJson.data.endDate}<br>
              Created Date: ${dataJson.data.createdDate}<br>
              Status: ${dataJson.data.status}<br>
             </div>`;


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
      var option = document.createElement("option");
        option.text = "Select dorm";
        option.disabled = true;
        option.selected = true;
        dormDropDown.append(option);
      jsonData.data.forEach(element => {
        var option = document.createElement("option");
        option.text = element.name;
        option.value = element.id;
        dormDropDown.append(option);
      });

    })
    // .then(
    //   loadrooms
    // )

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
  let url = "http://localhost:8081/api/v1/rooms/dorm/"+ dormId +"/gender" ;
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
        option.text = "Select room";
        option.disabled = true;
        option.selected = true;
        roomDropDown.append(option);
      jsonData.data.forEach(element => {
        var option = document.createElement("option");
        option.text = element.name;
        option.value = element.id;
        roomDropDown.append(option);
      });
      console.log(11);
    }
  
    )
    // .then(
    //   loadslots
    // )
    .catch(error => {
      console.error('Error:', error);
    });
}


function loadslots() {
  console.log(1);
  let slotDropDown = document.getElementById("slots");
  slotDropDown.innerHTML = '';
  const selectElement = document.getElementById("rooms");
  const roomId = selectElement.value;
  console.log("roomid for slot: " + roomId);
  let url = "http://localhost:8081/api/v1/slots/room/" + roomId + "/available";
  // console.log(url);
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
      option.text = "Select slot";
      option.disabled = true;
      option.selected = true;
      slotDropDown.append(option);
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
  let text = document.getElementById("bookingtitle");
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
      //  bookingRequest = `<h1>Keeping Day</h1>`
        text.innerHTML ="Keeping Day"


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
  let text = document.getElementById("bookingtitle");
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
        // bookingRequest = `<h3>Booking Day</h3>`
        text.innerHTML = "Booking Day"
        dormRequest = `
        <div id="booking-request-form">
        <div class=" no-padding no-margin">
            <h1 id="check"></h1>
            <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                <div class="SBB-available-btn" >
                    <a href="#" class="sidebar-linkItem flex items-center" style="height: 42px">See available beds</a>
                </div>
                <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin">Room Type</label>
                    <div class="" >
                        <input class="SBB-input " readonly="" type="text" value="SV - 3 beds - 950.000">
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
                <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin">Room</label>
                    <div class="my-select-style">
                        <select class="SBB-input" id="rooms" name="Rooms" onchange="loadslots()"></select>
                    </div>
                </div>
            </div>
            <div class="flex" style="gap: 24px;  margin-bottom: 24px;">
                <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin" for="Semester">Slot</label>
                    <div class="my-select-style">
                        <select class="SBB-input" id="slots" name="Slot" onchange="getBookingCost()">
                        </select>
                    </div>
                </div>
                
                <div class="SBB-layout-1">
                    <label class="SBB-input-label no-margin" for="Note">Note</label>
                    <div class="">
                        <input class="SBB-input text-box single-line" id="Note" name="Note" type="text" value="" style="height: 42px">
                        </input>
                    </div>
                </div>
                
            </div>
            <div id="booking">
            
            </div>
            
        </div>
    </div>`
          // < div class="SBB-layout-1" >
          //           <label class="SBB-input-label no-margin" for="Note">Note</label>
          //           <div class="my-select-style">
          //               <input class="SBB-input" id="Note" name="Note">
          //               </input>
          //           </div>
          //       </div >
          dorm.innerHTML = dormRequest;
        loaddorm();
      } else {
        //bookingRequest = `<h3 style="color:red;">Sorry, it's not right time to book a new bed!</h3>`
        text.innerHTML = "Sorry, it's not right time to book a new bed!"
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
  let url = "http://localhost:8081/api/v1/booking-requests/check-living/branchs/" + branchId;
  let text = document.getElementById("bookingtitle");
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
        // bookingRequest = `<h1>keeping day: 
        //                                   you don't have room</h1>`
        text.innerHTML = "keeping day: you don't have room"
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
                    <div class="no-padding no-margin">
                <h1 id="check"></h1>
                <div class="flex" style="gap: 48px;  margin-bottom: px;">
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
                      <option value="${dataJsondata.room.dorm.id}" disabled selected>${data.room.dorm.name}</option></select></select>
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
                    <input class="SBB-input text-box single-line" id="Note" name="Note" type="text" value="" style="height: 42px />
                  </div>
                </div>
              </div>
              <div id="booking">
            
            </div>
                  
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
        getBookingCost();
        checkBalance();
      } else {

      }

    })
    .catch(error => {
      console.error('Error:', error);
    });
}

function getBalance(){
  let url = "http://localhost:8081/api/v1/user-infos/balance";
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("jwt")}`
    }
  })
    .then(response => response.json())
    .then(jsonData => {
      document.getElementById("balance").innerHTML = jsonData.data.toLocaleString('en-US') + " VND";
    })
    .catch(error => {
      console.log("error");
    });
}

function getBookingCost(){
  console.log(222);
  slotid = document.getElementById("slots").value;
  let url = "http://localhost:8081/api/v1/user-infos/booking-cost?slotid="+slotid;
 
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("jwt")}`
    }
  })
    .then(response => response.json())
    .then(jsonData => {
      document.getElementById("cost").innerHTML = jsonData.data.toLocaleString('en-US') + " VND";
    }).then(checkBalance())
    .catch(error => {
      console.log("error");
    });
}

function checkBalance(){
 
  slotid = document.getElementById("slots").value;
  let url = "http://localhost:8081/api/v1/user-infos/check-balance?slotid="+slotid;
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
      if(jsonData.data == true){
        document.getElementById("booking").innerHTML = '<button onclick="addBookingRequests()"  class="orange-btn">Add</button>';
      }
      else{
        document.getElementById("booking").innerHTML = "Not enough balance.";
      }
    })
    .catch(error => {
      console.log("error");
    });
}