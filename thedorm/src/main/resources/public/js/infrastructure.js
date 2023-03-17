function loadFormAddDorm() {
    let formAddDorm = document.getElementById("add")
    let button = document.getElementById("button")
    button.innerHTML = ``
    formAddDorm.innerHTML = ` <label>Name</label>
    <input type="text" id="name"><br>
    <button class="btn btn-primary" type="submit" onclick="addDorm()">Add</button>`
}

function addDorm() {
    let branchId = document.getElementById("branchId").value
    let name = document.getElementById("name").value
    console.log(1);
    url = "http://localhost:8081/api/v1/dorms";
    jsonData = { name: name, branch: { id: branchId } };
    fetch(url,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(loadDormInBranch())
        .then(a())
        .catch(error => {
            console.error('Error:', error);
        });

}


function loadDormInBranch() {
    let head = document.getElementById("head")
    let infrastructureTable = document.getElementById("infrastructureTable")
    let table = ``
    let branchId = document.getElementById("branchId").value
    let button = document.getElementById("button")
    button.innerHTML = `<button class="btn btn-primary" type="submit" onclick="loadFormAddDorm()">Add dorm</button>`
    head.innerHTML = `
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Branch Name</th>
    </tr>`



    let url = "http://localhost:8081/api/v1/dorms/branch/" + branchId;

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
                table += `<tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.branch.name}</td>
                </tr>`;
                console.log(dataJson);
            });

            infrastructureTable.innerHTML = table;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}



function addRoom() {
    let dormId = document.getElementById("dormId").value
    let name = document.getElementById("name").value

    console.log(1);
    url = "http://localhost:8081/api/v1/rooms";
    jsonData = { name: name, dorm: { id: dormId }, basePrice: { id: 1 } };
    console.log(jsonData);
    fetch(url,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(loadRoomInDorm())
        .then(a())
        .catch(error => {
            console.error('Error:', error);
        });

}

function a() {
    let button = document.getElementById("button")
    button.innerHTML = ``
}

function loadFormAddRoom() {
    let formAddDorm = document.getElementById("add")
    let button = document.getElementById("button")
    button.innerHTML = ``
    formAddDorm.innerHTML = ` <label>Name</label>
    <input type="text" id="name"><br>
    <button class="btn btn-primary" type="submit" onclick="addRoom()">Add</button>`
}

function loadRoomInDorm() {

    let head = document.getElementById("head")
    let infrastructureTable = document.getElementById("infrastructureTable")
    let table = ``
    let dormId = document.getElementById("dormId").value
    console.log(dormId);
    console.log(1);
    let button = document.getElementById("button")
    button.innerHTML = `<button class="btn btn-primary" type="" onclick="loadFormAddRoom()">Add room</button>`
    head.innerHTML = `
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Dorm Name</th>
      <th>Branch Name</th>
      <th>Base Price</th>
      </tr>`



    let url = "http://localhost:8081/api/v1/rooms/dorm/" + dormId;

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
                table += `<tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.dorm.name}</td>
                <td>${element.dorm.branch.name}</td>
                <td>${element.basePrice.id}</td>
                
                </tr>`;
                console.log(dataJson);
            });

            infrastructureTable.innerHTML = table;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loadInfrastructure() {
    let add = document.getElementById("add")
    add.innerHTML = ``
    let button = document.getElementById("button")
    button.innerHTML = ``
    let head = document.getElementById("head")
    let infrastructureTable = document.getElementById("infrastructureTable")
    head.innerHTML = ``
    infrastructureTable.innerHTML = ``
    let idSelected = document.getElementById("infrastructure").value
    let form = document.getElementById("form")

    form.innerHTML = ``
    if (idSelected == 1) {
        form.innerHTML = `BranchID: <br><select class="SBB-input" id="branchId" onchange="loadDormInBranch()">
                 <option value="" disabled selected>Chọn một lựa chọn</option>
               </select><br>
       `

        loadbranch();
    } if (idSelected == 2) {
        form.innerHTML = `BranchID: <br><select class="SBB-input" id="branchId" onchange="loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormId" onchange="loadRoomInDorm()">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       `


        loadbranch();
    } if (idSelected == 3) {

        form.innerHTML = `BranchID: <br><select class="SBB-input" id="branchId" onchange="loaddorm()">
    <option value="" disabled selected>Chọn một lựa chọn</option>
  </select><br> 
  DormID: <br><select class="SBB-input" id="dormId" onchange="loadrooms()">
       <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
       RoomID: <br><select class="SBB-input" id="roomId" onchange="loadSlotInRoom()">
         <option value="" disabled selected>Chọn một lựa chọn</option>
       </select><br>
      `
        loadbranch();
    }
}

function addSlot() {
    console.log("add");
    let roomId = document.getElementById("roomId").value
    let name = document.getElementById("name").value

    console.log(1);
    url = "http://localhost:8081/api/v1/slots";
    jsonData = { name: name, room: { id: roomId } };
    console.log(jsonData);
    fetch(url,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(loadSlotInRoom())
        .then(a())
        .catch(error => {
            console.error('Error:', error);
        });

}

function loadFormAddSlot() {
    let formAddSlot = document.getElementById("add")
    let button = document.getElementById("button")
    button.innerHTML = ``
    formAddSlot.innerHTML = ` <label>Name</label>
    <input type="text" id="name"><br>
    <button class="btn btn-primary" type="submit" onclick="addSlot()">Add</button>`
}

function loadSlotInRoom() {
    let head = document.getElementById("head")
    let infrastructureTable = document.getElementById("infrastructureTable")
    let table = ``
    let roomId = document.getElementById("roomId").value
    console.log(roomId);
    console.log(1);
    let button = document.getElementById("button")
    button.innerHTML = `<button class="btn btn-primary" type="" onclick="loadFormAddSlot()">Add slot</button>`
    head.innerHTML = `
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Room Name</th>
      <th>Dorm Name</th>
      <th>Branch Name</th>
      </tr>`
    let url = "http://localhost:8081/api/v1/slots/room/" + roomId;
    console.log(url);
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
                table += `<tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.room.name}</td>
                <td>${element.room.dorm.name}</td>
                <td>${element.room.dorm.branch.name}</td>
                </tr>`;
                console.log(dataJson);
            });

            infrastructureTable.innerHTML = table;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loadbranch() {
    let branchDropDown = document.getElementById("branchId");
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
    console.log(1);
    let dormDropDown = document.getElementById("dormId");
    dormDropDown.innerHTML = '';
    const branchId = document.getElementById("branchId").value;
    console.log(branchId);
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

        .catch(error => {
            console.error('Error:', error);
        });
}


function loadrooms() {
    let roomDropDown = document.getElementById("roomId");
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dormId");
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

        .catch(error => {
            console.error('Error:', error);
        });
}


