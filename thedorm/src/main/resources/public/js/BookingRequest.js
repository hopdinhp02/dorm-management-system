
document.addEventListener("DOMContentLoaded", function () {
    loadBooking()
});


function loadBooking() {
    let url = "http://localhost:8081/api/v1/booking-requests";
    let bookingTable = document.getElementById("booking");
    let bookingRequest = ``;
    fetch(url)
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
                     <option value="0" ${element.status==0?"selected":""}>0</option>
                     <option value="1"  ${element.status==1?"selected":""}>1</option>
                     <option value="2"  ${element.status==2?"selected":""}>2</option>
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
        updateBookingRequest(id,value)
    } else {
        setTimeout(loadBooking(),500)
        
        
    }
}


function updateBookingRequest(id, value){
    url = "http://localhost:8081/api/v1/booking-requests/"+id
    let bookingRequestStatus = value
        jsonData = { status: bookingRequestStatus  };
    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        }).catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadBooking, 50);
      } 
    




      let branchDropDown = document.getElementById("branchs");

     
      
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
                  loadbeds
              )   
              .catch(error => {
                  console.error('Error:', error);
              });
      } 
      
      let bedDropDown = document.getElementById("beds");
      function loadbeds() {
          bedDropDown.innerHTML = '';
          const selectElement = document.getElementById("rooms");
          const roomId = selectElement.value;
          console.log("roomid for bed: "+roomId);
          let url = "http://localhost:8081/api/v1/beds/room/" + roomId;
          fetch(url)
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
                  console.log( "error");
              });
      } 
      
        
      function addBookingRequests() {
          url = "http://localhost:8081/api/v1/booking-requests";
          ResidentID = document.getElementById("ResidentID").value;
          BedID = document.getElementById("BedID").value;
          Note = document.getElementById("Note").value; 
          StartDate = document.getElementById("startdate").value;
          EndDate = document.getElementById("EndDate").value;
          CreatedDate = document.getElementById("CreatedDate").value;
          Status = document.getElementById("Status").value;
          console.log(ResidentID);
          jsonData = { userInfo:{id: ResidentID} ,bed:{id : BedID} , note: Note,startDate: StartDate,
          endDate: EndDate ,createdDate: CreatedDate, status: Status }; 
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
              .then(loadBooking)
              .catch(error => {
                  console.error('Error:', error);
              });
      }