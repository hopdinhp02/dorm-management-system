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

function loadFacility() {
    console.log(1);
    let value = document.getElementById("facility").value
    console.log(value);
    if (value == 1) {
        loadFacilityAssign()
    } else if (value == 2) {
        loadFacilityNotAssign()
    }

}

function loadFacilityAssign() {
    let showBy = document.getElementById("hopnghien")
    showBy.innerHTML = ` Show By: <br><select class="SBB-input" id="idRemove" onchange="loadIdRemove()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
    <option value="1" >Branch</option>
    <option value="2" >Dorm</option>
    <option value="3" >Room</option>
    <option value="4" >Slot</option>
  </select><br>`
 
}

function loadFacilityNotAssign() {
    console.log(3);
    let facilityTable = document.getElementById("facilityTable")
    facilityTable.innerHTML=``
    let facility = ``
    let showBy = document.getElementById("hopnghien")
    showBy.innerHTML = ``
    let url = "http://localhost:8081/api/v1/facilities/not-assign";
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
                facility += ` 
                
        <tbody id="facilityDetail">
        <tr>
        <td>${element.facilityDetail.id}</td>
        <td>${element.facilityDetail.name==null?"":element.facilityDetail.name}</td>
        <td>${element.facilityDetail.expirationDate==null?"":element.facilityDetail.expirationDate}</td>
        <td>${element.facilityDetail.codeProduct==null?"":element.facilityDetail.codeProduct}</td>
        <td>${element.facilityDetail.price==null?"":element.facilityDetail.price}</td>
        <td>${element.facilityDetail.producingDate==null?"":element.facilityDetail.producingDate}</td>
        <td>${element.facilityDetail.provider==null?"":element.facilityDetail.provider}</td>
        <td>${element.facilityDetail.type==null?"":element.facilityDetail.type}</td>
        <td>
        <select onchange=" accept(${element.id}, this.value)">
             <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
             <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
             <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
        </select>
        </td>
        <td><button  class="btn btn-primary" type="submit" onclick="moveToUpdate(${element.facilityDetail.id})">Update</button> <button class="btn btn-primary" type="submit" onclick="moveToMaintenance(${element.id})">maintenance</button> 
        <button  class="btn btn-primary" type="submit" onclick="moveToRemove(${element.id})">Remove</button></td>

        </tr>`;
                facilityTable.innerHTML = facility;
            });

        })
        .catch(error => {
            console.log("error");
        });
}
function updateFacilityDetail(id, value) {
    let value1 = document.getElementById("facility").value
    url = "http://localhost:8081/api/v1/facilities/"+ id  +"/facility-detail " 
    let facilityDetailStatus = value
    jsonData = { facilityDetail: { status: facilityDetailStatus }};
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
        .catch(error => {
            console.error('Error:', error);
        });
        if (value1 == 1) {
    setTimeout(loadFacilityAssign, 500);
            
        } else {
    setTimeout(loadFacilityNotAssign, 500);
        }
}

function accept(id, value) {
    let value1 = document.getElementById("facility").value

    if (confirm("The value has changed to: " + value)) {
        updateFacilityDetail(id, value)
    } else if(value1 == 1 ){
        setTimeout(loadFacilityAssign(), 500)
    } else if(value1 == 2 ){
        setTimeout(loadFacilityNotAssign(), 500)
    } 
}

function loadIdRemove() {
    let idSelected = document.getElementById("idRemove").value
    let removeDropDown = document.getElementById("load")
    let reMoveRequest = ``
    if (idSelected == 1) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange="loadFacilityByBranch()">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `
        removeDropDown.innerHTML = reMoveRequest
        loadbranch();
    } if (idSelected == 2) {
        reMoveRequest = `BranchID: <br><select class="SBB-input" id="branchRemove" onchange=" loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormRemove" onchange="loadFacilityByDorm()">
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
       RoomID: <br><select class="SBB-input" id="roomRemove" onchange="loadFacilityByRoom()">
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
           SlotID: <br><select class="SBB-input" id="slotRemove" onchange="loadFacilityBySlot()">
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
            var option = document.createElement("option");
                option.text = "Chọn một lựa chọn"
                option.value = ""
                option.disabled = true
                option.selected = true
                dormDropDown.append(option);
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
            var option = document.createElement("option");
                option.text = "Chọn một lựa chọn"
                option.value = ""
                option.disabled = true
                option.selected = true
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


function loadFacilityByBranch() {
    console.log(3);
    let facilityTable = document.getElementById("facilityTable")
    facilityTable.innerHTML = ``
    let facility = ``
    let id = document.getElementById("branchRemove").value
    let url = "http://localhost:8081/api/v1/facilities/branchs/"+id;
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
                facility += ` 
                
        <tbody id="facilityDetail">
        <tr>
        <td>${element.facilityDetail.id}</td>
        <td>${element.facilityDetail.name==null?"":element.facilityDetail.name}</td>
        <td>${element.facilityDetail.expirationDate==null?"":element.facilityDetail.expirationDate}</td>
        <td>${element.facilityDetail.codeProduct==null?"":element.facilityDetail.codeProduct}</td>
        <td>${element.facilityDetail.price==null?"":element.facilityDetail.price}</td>
        <td>${element.facilityDetail.producingDate==null?"":element.facilityDetail.producingDate}</td>
        <td>${element.facilityDetail.provider==null?"":element.facilityDetail.provider}</td>
        <td>${element.facilityDetail.type==null?"":element.facilityDetail.type}</td>
        <td>
        <select onchange=" accept(${element.id}, this.value)">
             <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
             <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
             <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
        </select>
        </td>
        <td><button  class="btn btn-primary" type="submit" onclick="moveToUpdate(${element.facilityDetail.id})">Update</button> <button class="btn btn-primary" type="submit" onclick="moveToMaintenance(${element.id})">maintenance</button> <button  class="btn btn-primary" type="submit" onclick="moveToRemove(${element.id})">Remove</button></td>

        </tr>`;
                facilityTable.innerHTML = facility;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function loadFacilityByDorm() {
    let facilityTable = document.getElementById("facilityTable")
    facilityTable.innerHTML = ``
    let facility = ``
    let id = document.getElementById("dormRemove").value
    let url = "http://localhost:8081/api/v1/facilities/dorms/"+id;
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
                facility += ` 
                
        <tbody id="facilityDetail">
        <tr>
        <td>${element.facilityDetail.id}</td>
        <td>${element.facilityDetail.name==null?"":element.facilityDetail.name}</td>
        <td>${element.facilityDetail.expirationDate==null?"":element.facilityDetail.expirationDate}</td>
        <td>${element.facilityDetail.codeProduct==null?"":element.facilityDetail.codeProduct}</td>
        <td>${element.facilityDetail.price==null?"":element.facilityDetail.price}</td>
        <td>${element.facilityDetail.producingDate==null?"":element.facilityDetail.producingDate}</td>
        <td>${element.facilityDetail.provider==null?"":element.facilityDetail.provider}</td>
        <td>${element.facilityDetail.type==null?"":element.facilityDetail.type}</td>
        <td>
        <select onchange=" accept(${element.id}, this.value)">
             <option>${element.facilityDetail.status}</option>
             <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
             <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
             <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
        </select>
        </td>
        <td><button  class="btn btn-primary" type="submit" onclick="moveToUpdate(${element.facilityDetail.id})">Update</button> <button class="btn btn-primary" type="submit" onclick="moveToMaintenance(${element.id})">maintenance</button> <button  class="btn btn-primary" type="submit" onclick="moveToRemove(${element.id})">Remove</button></td>


        </tr>`;
                facilityTable.innerHTML = facility;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function loadFacilityByRoom() {
    let facilityTable = document.getElementById("facilityTable")
    facilityTable.innerHTML = ``

    let facility = ``
    let id = document.getElementById("roomRemove").value
    let url = "http://localhost:8081/api/v1/facilities/rooms/"+id;
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
                facility += ` 
                
        <tbody id="facilityDetail">
        <tr>
        <td>${element.facilityDetail.id}</td>
        <td>${element.facilityDetail.name==null?"":element.facilityDetail.name}</td>
        <td>${element.facilityDetail.expirationDate==null?"":element.facilityDetail.expirationDate}</td>
        <td>${element.facilityDetail.codeProduct==null?"":element.facilityDetail.codeProduct}</td>
        <td>${element.facilityDetail.price==null?"":element.facilityDetail.price}</td>
        <td>${element.facilityDetail.producingDate==null?"":element.facilityDetail.producingDate}</td>
        <td>${element.facilityDetail.provider==null?"":element.facilityDetail.provider}</td>
        <td>${element.facilityDetail.type==null?"":element.facilityDetail.type}</td>
        <td>
        <select onchange=" accept(${element.id}, this.value)">
             <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
             <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
             <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
        </select>
        </td>
        <td><button  class="btn btn-primary" type="submit" onclick="moveToUpdate(${element.facilityDetail.id})">Update</button> <button class="btn btn-primary" type="submit" onclick="moveToMaintenance(${element.id})">maintenance</button> <button  class="btn btn-primary" type="submit" onclick="moveToRemove(${element.id})">Remove</button></td>

        
        </tr>`;
                facilityTable.innerHTML = facility;
            });

        })
        .catch(error => {
            console.log("error");
        });
}

function loadFacilityBySlot() {
    let facilityTable = document.getElementById("facilityTable")
    facilityTable.innerHTML = ``
    let facility = ``
    let id = document.getElementById("slotRemove").value
    let url = "http://localhost:8081/api/v1/facilities/slots/"+id;
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
                facility += ` 
                
        <tbody id="facilityDetail">
        <tr>
        <td>${element.facilityDetail.id}</td>
        <td>${element.facilityDetail.name==null?"":element.facilityDetail.name}</td>
        <td>${element.facilityDetail.expirationDate==null?"":element.facilityDetail.expirationDate}</td>
        <td>${element.facilityDetail.codeProduct==null?"":element.facilityDetail.codeProduct}</td>
        <td>${element.facilityDetail.price==null?"":element.facilityDetail.price}</td>
        <td>${element.facilityDetail.producingDate==null?"":element.facilityDetail.producingDate}</td>
        <td>${element.facilityDetail.provider==null?"":element.facilityDetail.provider}</td>
        <td>${element.facilityDetail.type==null?"":element.facilityDetail.type}</td>
        <td>
        <select onchange=" accept(${element.id}, this.value)">
             <option>${element.facilityDetail.status}</option>
             <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
             <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
             <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
        </select>
        </td>
        <td><button  class="btn btn-primary" type="submit" onclick="moveToUpdate(${element.facilityDetail.id})">Update</button> <button class="btn btn-primary" type="submit" onclick="moveToMaintenance(${element.id})">maintenance</button> <button  class="btn btn-primary" type="submit" onclick="moveToRemove(${element.id})">Remove</button></td>

        </tr>`;
                facilityTable.innerHTML = facility;
            });

        })
        .catch(error => {
            console.log("error");
        });
}


function moveToUpdate(id) {
    const data = id;

  // Chuyển đến trang mới với dữ liệu
  window.location.href = 'updateFacility.html?data=' + encodeURIComponent(data);
    
}



function moveToMaintenance(id){
   
    const data = id;

    // Chuyển đến trang mới với dữ liệu
    window.location.href = 'maintenanceFacility.html?data=' + encodeURIComponent(data);
}



function moveToRemove(id){
   
    const data = id;

    // Chuyển đến trang mới với dữ liệu
    window.location.href = 'removeFacility2.html?data=' + encodeURIComponent(data);
}






