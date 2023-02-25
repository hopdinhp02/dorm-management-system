let branchDropDown = document.getElementById("branchs");

document.addEventListener("DOMContentLoaded", function () {
    loadbranch()
});

function loadbranch() {
    let url = "http://localhost:8081/api/v1/branchs";
    fetch(url)
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


let dormDropDown = document.getElementById("dorms");
function loaddorm() {
    dormDropDown.innerHTML = '';
    const selectElement = document.getElementById("branchs");
    const branchId = selectElement.value;
    console.log("branchid: "+branchId);
    let url = "http://localhost:8081/api/v1/dorms/branch/" + branchId;
    
    fetch(url)
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

let roomDropDown = document.getElementById("rooms");
function loadrooms() {
    roomDropDown.innerHTML = '';
    const selectElement = document.getElementById("dorms");
    const dormId = selectElement.value;
    console.log("dormid: "+dormId);
    let url = "http://localhost:8081/api/v1/rooms/dorm/" + dormId;
    fetch(url)
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

let slotDropDown = document.getElementById("slots");
function loadslots() {
    slotDropDown.innerHTML = '';
    const selectElement = document.getElementById("rooms");
    const roomId = selectElement.value;
    console.log("roomid for slot: "+roomId);
    let url = "http://localhost:8081/api/v1/slots/room/" + roomId;
    fetch(url)
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
            console.log( "error");
        });
} 

  
function addBookingRequests() {
    url = "http://localhost:8081/api/v1/booking-requests";
    ResidentID = document.getElementById("ResidentID").value;
    slotID = document.getElementById("slots").value;
    Note = document.getElementById("Note").value; 
    StartDate = document.getElementById("startdate").value;
    EndDate = document.getElementById("EndDate").value;
    CreatedDate = document.getElementById("CreatedDate").value;
    console.log(ResidentID);
    jsonData = { userInfo:{id: ResidentID} ,slot:{id : slotID} , note: Note,startDate: StartDate,
    endDate: EndDate ,createdDate: CreatedDate}; 
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
        .then(loadProduct)
        .catch(error => {
            console.error('Error:', error);
        });
}