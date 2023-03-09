document.addEventListener("DOMContentLoaded", function () {
    loadFacilityID()
    
});

function loadFacilityID() {
    let facilityDropDown = document.getElementById("facility");
    let url = "http://localhost:8081/api/v1/facilities";
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
                option.text = element.id;
                option.value = element.id;
                facilityDropDown.appendChild(option);
            });
            
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function loadFacility() {
    let idSelected = document.getElementById("facility").value;
    let url = "http://localhost:8081/api/v1/facilities/" + idSelected;
    let facility = ``;
    let facilityTable = document.getElementById("facilityTable")
    fetch(url)
        .then(response => response.json())
        
        .then(dataJson => {
                facility += `<tr>
                <td id="facilityID">${dataJson.data.id}</td>
                <td>${dataJson.data.branch == null ? null : dataJson.data.branch.id }</td>
                <td>${dataJson.data.dorm == null ? null : dataJson.data.dorm.id }</td>
                <td>${dataJson.data.room == null ? null : dataJson.data.room.id }</td>
                <td>${dataJson.data.slot == null ? null : dataJson.data.slot.id }</td>
                <td>${dataJson.data.facilityDetail == null ? null : dataJson.data.facilityDetail.id }</td>
                </tr>`;
            facilityTable.innerHTML = facility;


        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loadIdRemove(){
    let idSelected = document.getElementById("idRemove").value
    let removeDropDown = document.getElementById("load")
    let reMoveRequest = ``
    if (idSelected ==1) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange="">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `
               removeDropDown.innerHTML = reMoveRequest
               loadbranch();
    } if (idSelected == 2 ) {
    reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormRemove" onchange="">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       `
  removeDropDown.innerHTML = reMoveRequest
  loadbranch();
    }if (idSelected == 3) {
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
    }if (idSelected == 4) {
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

function removeFacility (){
    let facilityID = document.getElementById("facility").value
    console.log(facilityID);
    url = "http://localhost:8081/api/v1/facilities/" + facilityID
    
    if (document.getElementById("slotRemove")!=undefined) {
    let slotId = document.getElementById("slotRemove").value

     jsonData = { slot:{id:slotId}};
    } else if (document.getElementById("roomRemove")!=undefined) {
        let roomId = document.getElementById("roomRemove").value
    
         jsonData = { room:{id:roomId}};
        }else if (document.getElementById("dormRemove")!=undefined) {
            let dormId = document.getElementById("dormRemove").value
        
             jsonData = { dorm:{id:dormId}};
            }else if (document.getElementById("branchRemove")!=undefined) {
                let branchId = document.getElementById("branchRemove").value
            
                 jsonData = { branch:{id:branchId}};
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