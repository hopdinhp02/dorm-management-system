const urlParams = new URLSearchParams(window.location.search);
let data = urlParams.get('data');
loadFacility()
function loadFacility() {
    console.log(1);
    let url = "http://localhost:8081/api/v1/facilities/" + data;
    let facility = ``;
    let facilityTable = document.getElementById("facilityTable")
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    })
        .then(response => response.json())
        .then(dataJson => {
            console.log(2);
            if (dataJson.data.branch != null) {
                console.log(3);
                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td>${dataJson.data.branch.name}</td>
                <td></td>
                <td></td>
                <td></td>
                <td>${dataJson.data.facilityDetail == "" ? null : dataJson.data.facilityDetail.id}</td>
                </tr>`;
                facilityTable.innerHTML = facility;

            } else if (dataJson.data.dorm != null) {
                console.log(4);

                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td>${dataJson.data.dorm.branch.name}</td>
                <td>${dataJson.data.dorm.name}</td>
                <td></td>
                <td></td>
                <td>${dataJson.data.facilityDetail == "" ? null : dataJson.data.facilityDetail.id}</td>
                </tr>`;
                facilityTable.innerHTML = facility;

            } else if (dataJson.data.room != null) {
                console.log(5);

                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td>${dataJson.data.room.dorm.branch.name}</td>
                <td>${dataJson.data.room.dorm.name}</td>
                <td>${dataJson.data.room.name}</td>
                <td></td>
                <td>${dataJson.data.facilityDetail == "" ? null : dataJson.data.facilityDetail.id}</td>
                </tr>`;
                facilityTable.innerHTML = facility;

            } else if (dataJson.data.slot != null) {
                console.log(6);

                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td>${dataJson.data.slot.room.dorm.branch.name}</td>
                <td>${dataJson.data.slot.room.dorm.name}</td>
                <td>${dataJson.data.slot.room.name}</td>
                <td>${dataJson.data.slot.name}</td>
                <td>${dataJson.data.facilityDetail == "" ? null : dataJson.data.facilityDetail.id}</td>
                </tr>`;
                facilityTable.innerHTML = facility;

            }
            else{
                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>${dataJson.data.facilityDetail == "" ? null : dataJson.data.facilityDetail.id}</td>
                </tr>`;
                facilityTable.innerHTML = facility;
            }
        })

        .catch(error => {
            console.error('Error:', error);
        });
}

function loadIdRemove() {
    let idSelected = document.getElementById("idRemove").value
    let removeDropDown = document.getElementById("load")
    let reMoveRequest = ``
    if (idSelected == 1) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange="">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `
        removeDropDown.innerHTML = reMoveRequest
        loadbranch();
    } if (idSelected == 2) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormRemove" onchange="">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       `
        removeDropDown.innerHTML = reMoveRequest
        loadbranch();
    } if (idSelected == 3) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormRemove" onchange="loadrooms()">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       RoomID: <br><select class="SBB-input" id="roomRemove" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
      `
        removeDropDown.innerHTML = reMoveRequest
        loadbranch();
    } if (idSelected == 4) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange=" loaddorm()">
        <option value="" disabled selected>Chọn một lựa chọn</option>
      </select><br> 
      DormID: <br><select class="SBB-input" id="dormRemove" onchange="loadrooms()">
           <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           RoomID: <br><select class="SBB-input" id="roomRemove" onchange=" loadslots()">
             <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           SlotID: <br><select class="SBB-input" id="slotRemove" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br></br>`
        removeDropDown.innerHTML = reMoveRequest
        loadbranch();
    }if(idSelected == 5){
        reMoveRequest =``
        removeDropDown.innerHTML = reMoveRequest
    }
}

function loadbranch() {
    let branchDropDown = document.getElementById("branchRemove");
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
    let dormDropDown = document.getElementById("dormRemove");
    dormDropDown.innerHTML = '';
    const selectElement = document.getElementById("branchRemove");
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
    let roomDropDown = document.getElementById("roomRemove");
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dormRemove");
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
    let slotDropDown = document.getElementById("slotRemove");
    slotDropDown.innerHTML = '';
    const selectElement = document.getElementById("roomRemove");
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

function removeFacility() {

    url = "http://localhost:8081/api/v1/facilities/" + data

    if (document.getElementById("slotRemove") != undefined) {
        let slotId = document.getElementById("slotRemove").value

        jsonData = { slot: { id: slotId } };
        console.log(jsonData);
    } else if (document.getElementById("roomRemove") != undefined) {
        let roomId = document.getElementById("roomRemove").value

        jsonData = { room: { id: roomId } };
    } else if (document.getElementById("dormRemove") != undefined) {
        let dormId = document.getElementById("dormRemove").value

        jsonData = { dorm: { id: dormId } };
    } else if (document.getElementById("branchRemove") != undefined) {
        let branchId = document.getElementById("branchRemove").value

        jsonData = { branch: { id: branchId } };
    }
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
        .then(loadFacility)
        .catch(error => {
            console.error('Error:', error);
        });

}

function notAssignFacility() {
    url = "http://localhost:8081/api/v1/facilities/" + data
        jsonData = {slot: null, branch: null, dorm:null, room: null};
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
        .then(loadFacility)
        .catch(error => {
            console.error('Error:', error);
        });

}