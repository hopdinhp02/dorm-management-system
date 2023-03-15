loadFacility();


function loadFacility() {
    let url = "http://localhost:8081/api/v1/facilities";
    let facilityTable = document.getElementById("facility");
    let facilityRequest = ``;
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
                facilityRequest += `<tr>
                <td>${element.id}</td>
                <td>${element.branch == null ? null : element.branch.id }</td>
                <td>${element.dorm == null ? null : element.dorm.id}</td>
                <td>${element.room == null ? null : element.room.id}</td>
                <td>${element.slot == null ? null : element.slot.id}</td>
                <td onclick="loadfacilityDetailById(${element.id})"> ${element.facilityDetail.id} </td>
                </tr>`;
                
            });

            facilityTable.innerHTML = facilityRequest;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function addFacility() {
    console.log(1);
    url = "http://localhost:8081/api/v1/facilities";
    codeProduct = document.getElementById("codeProduct").value;
    Name = document.getElementById("name").value;
    Price = document.getElementById("price").value;
    Provider = document.getElementById("provider").value;
    ExpirationDate = document.getElementById("expirationDate").value;
    procudingDate = document.getElementById("procudingDate").value;
    Status = document.getElementById("status").value;
    Type = document.getElementById("type").value;
    Value = document.getElementById("value").value;
    
    jsonData = {
        facilityDetail:{codeProduct: codeProduct, 
        name: Name,
        price: Price, 
        provider: Provider, 
        expirationDate: ExpirationDate, 
        procudingDate: procudingDate, 
        status: Status, 
        type: Type, 
        value: Value }};
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
        .then(loadFacility)
        .catch(error => {
            console.error('Error:', error);
        });
}

// Code Product: <input type="text" id="codeProduct"><br>
// Name: <input type="text" id="name"><br>
// Price: <input type="number" id="price"><br>
// Provider: <input type="text" id="provider"><br>
// Expiration Date: <input type="datetime-local" id="expirationDate"><br>
// Procuding Date: <input type="datetime-local" id="procudingDate"><br>
// Status: <input type="text" id="status"><br>
// Type: <input type="text" id="type"><br>
// Value: <input type="text" id="value"><br>
function loadfacilityDetailById(id) {
    let url = "http://localhost:8081/api/v1/facilities/" + id;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            
            let facilitySelected = dataJson.data.facilityDetail
            document.getElementById("codeProduct").value = facilitySelected.codeProduct;
            document.getElementById("name").value = facilitySelected.name;
            document.getElementById("price").value = facilitySelected.price;
            document.getElementById("provider").value = facilitySelected.provider;
            document.getElementById("expirationDate").value = facilitySelected.expirationDate;
            document.getElementById("procudingDate").value = facilitySelected.procudingDate;
            document.getElementById("status").value = facilitySelected.status;
            document.getElementById("type").value = facilitySelected.type;
            document.getElementById("value").value = facilitySelected.value;
            
            updateBTN = document.getElementById("update");
            deleteBTN = document.getElementById("delete");
            deleteBTN.onclick = function(){
                deleteDorm(id)
            };
            updateBTN.onclick = function(){
                updateFacility(id)
            };



        })
        .catch(error => {
            console.error('Error:', error);
        });
}




function updateFacility(id, value) {
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
