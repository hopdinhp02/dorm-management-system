function loadIdSearch(){
    let idSelected = document.getElementById("idSearch").value
    let searchDropDown = document.getElementById("load")
    let searchRequest = ``
    if (idSelected ==1) {
        searchRequest = `BranchID: <br><select class="SBB-input" id="branchSearch" onchange="">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `
               searchDropDown.innerHTML = searchRequest
               loadbranch();
    } if (idSelected == 2 ) {
    searchRequest = `BranchID: <br><select class="SBB-input" id="branchSearch" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormSearch" onchange="">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       `
  searchDropDown.innerHTML = searchRequest
  loadbranch();
    }if (idSelected == 3) {
        searchRequest = `BranchID: <br><select class="SBB-input" id="branchSearch" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormSearch" onchange="loadrooms()">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       RoomID: <br><select class="SBB-input" id="roomSearch" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
      `
  searchDropDown.innerHTML = searchRequest
  loadbranch();
    }if (idSelected == 4) {
        searchRequest = `BranchID: <br><select class="SBB-input" id="branchSearch" onchange=" loaddorm()">
        <option value="" disabled selected>Chọn một lựa chọn</option>
      </select><br> 
      DormID: <br><select class="SBB-input" id="dormSearch" onchange="loadrooms()">
           <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           RoomID: <br><select class="SBB-input" id="roomSearch" onchange=" loadslots()">
             <option value="" disabled selected>Chọn một lựa chọn</option>
           </select><br>
           SlotID: <br><select class="SBB-input" id="slotSearch" onchange="">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br></br>`
      searchDropDown.innerHTML = searchRequest
      loadbranch();
    }
}

function loadbranch() {
    let branchDropDown = document.getElementById("branchSearch");
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
    let dormDropDown = document.getElementById("dormSearch");
    dormDropDown.innerHTML = '';
    const selectElement = document.getElementById("branchSearch");
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
    let roomDropDown = document.getElementById("roomSearch");
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dormSearch");
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
    let slotDropDown = document.getElementById("slotSearch");
    slotDropDown.innerHTML = '';
    const selectElement = document.getElementById("roomSearch");
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


function searchResident() {
    let name = document.getElementById("name").value
    let idSelected = document.getElementById("idSearch").value
    if (idSelected == 1) {
        console.log(1);
        searchInBranch(name)
    }else if(idSelected ==2){
        console.log(2);
        searchInDorm(name)
    }else if(idSelected ==3){
        console.log(3);
        searchInRoom(name)
    }else if(idSelected ==4){
        console.log(4);
        searchInSlot(name)
    }else if(idSelected ==5){
        console.log(5);
        searchInAll(name)
    }

}

function searchInAll(name){
    let url = "http://localhost:8081/api/v1/resident-histories/find"+`?name=${name}`;
    let residentTable = document.getElementById("viewResident")
    residentTable.innerHTML= ``
    let resident = ``
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
                resident += `<tr>
                
                <td>${element.userInfo.id}</td>
                <td>${element.userInfo.name == null?"": element.userInfo.name}</td>
                <td>${element.userInfo.email == null?"": element.userInfo.email}</td>
                <td>${element.userInfo.phone == null?"": element.userInfo.phone}</td>
                <td>${element.userInfo.image == null?"": element.userInfo.image}</td>
                <td>${element.slot.room.dorm.branch.name == null?"": element.slot.room.dorm.branch.name}</td>
                <td>${element.slot.room.dorm.name == null?"": element.slot.room.dorm.name}</td>
                <td>${element.slot.room.name == null?"": element.slot.room.name}</td>
                <td>${element.slot.name == null?"": element.slot.name}</td>
                </tr>`;
                residentTable.innerHTML = resident;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function searchInBranch(name) {
    let branchId = document.getElementById("branchSearch").value
    let url = "http://localhost:8081/api/v1/resident-histories/find-by-branch"+`?name=${name}&branchid=${branchId}`;
    let residentTable = document.getElementById("viewResident")
    residentTable.innerHTML= ``
    let resident = ``
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
                resident += `<tr>
                
                <td>${element.userInfo.id}</td>
                <td>${element.userInfo.name == null?"": element.userInfo.name}</td>
                <td>${element.userInfo.email == null?"": element.userInfo.email}</td>
                <td>${element.userInfo.phone == null?"": element.userInfo.phone}</td>
                <td>${element.userInfo.image == null?"": element.userInfo.image}</td>
                <td>${element.slot.room.dorm.branch.name == null?"": element.slot.room.dorm.branch.name}</td>
                <td>${element.slot.room.dorm.name == null?"": element.slot.room.dorm.name}</td>
                <td>${element.slot.room.name == null?"": element.slot.room.name}</td>
                <td>${element.slot.name == null?"": element.slot.name}</td>
                </tr>`;
                residentTable.innerHTML = resident;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function searchInDorm(name) {
    let dormId = document.getElementById("dormSearch").value
    let url = "http://localhost:8081/api/v1/resident-histories/find-by-dorm"+`?name=${name}&dormid=${dormId}`;
    let residentTable = document.getElementById("viewResident")
    residentTable.innerHTML= ``
    let resident = ``
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
                resident += `<tr>
                
                <td>${element.userInfo.id}</td>
                <td>${element.userInfo.name == null?"": element.userInfo.name}</td>
                <td>${element.userInfo.email == null?"": element.userInfo.email}</td>
                <td>${element.userInfo.phone == null?"": element.userInfo.phone}</td>
                <td>${element.userInfo.image == null?"": element.userInfo.image}</td>
                <td>${element.slot.room.dorm.branch.name == null?"": element.slot.room.dorm.branch.name}</td>
                <td>${element.slot.room.dorm.name == null?"": element.slot.room.dorm.name}</td>
                <td>${element.slot.room.name == null?"": element.slot.room.name}</td>
                <td>${element.slot.name == null?"": element.slot.name}</td>
                </tr>`;
                residentTable.innerHTML = resident;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function searchInRoom(name) {
    let roomId = document.getElementById("roomSearch").value
    let url = "http://localhost:8081/api/v1/resident-histories/find-by-room"+`?name=${name}&roomid=${roomId}`;
    let residentTable = document.getElementById("viewResident")
    residentTable.innerHTML= ``
    let resident = ``
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
                resident += `<tr>
                
                <td>${element.userInfo.id}</td>
                <td>${element.userInfo.name == null?"": element.userInfo.name}</td>
                <td>${element.userInfo.email == null?"": element.userInfo.email}</td>
                <td>${element.userInfo.phone == null?"": element.userInfo.phone}</td>
                <td>${element.userInfo.image == null?"": element.userInfo.image}</td>
                <td>${element.slot.room.dorm.branch.name == null?"": element.slot.room.dorm.branch.name}</td>
                <td>${element.slot.room.dorm.name == null?"": element.slot.room.dorm.name}</td>
                <td>${element.slot.room.name == null?"": element.slot.room.name}</td>
                <td>${element.slot.name == null?"": element.slot.name}</td>
                </tr>`;
                residentTable.innerHTML = resident;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function searchInSlot(name) {
    let slotId = document.getElementById("slotSearch").value
    let url = "http://localhost:8081/api/v1/resident-histories/find-by-slot"+`?name=${name}&slotid=${slotId}`;
    let residentTable = document.getElementById("viewResident")
    residentTable.innerHTML= ``
    let resident = ``
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
                resident += `<tr>
                
                <td>${element.userInfo.id}</td>
                <td>${element.userInfo.name == null?"": element.userInfo.name}</td>
                <td>${element.userInfo.email == null?"": element.userInfo.email}</td>
                <td>${element.userInfo.phone == null?"": element.userInfo.phone}</td>
                <td>${element.userInfo.image == null?"": element.userInfo.image}</td>
                <td>${element.slot.room.dorm.branch.name == null?"": element.slot.room.dorm.branch.name}</td>
                <td>${element.slot.room.dorm.name == null?"": element.slot.room.dorm.name}</td>
                <td>${element.slot.room.name == null?"": element.slot.room.name}</td>
                <td>${element.slot.name == null?"": element.slot.name}</td>
                </tr>`;
                residentTable.innerHTML = resident;
            });

        })
        .catch(error => {
            console.log("error");
        });
}