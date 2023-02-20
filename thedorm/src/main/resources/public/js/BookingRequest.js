
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
    




