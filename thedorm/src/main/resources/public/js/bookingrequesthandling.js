document.addEventListener("DOMContentLoaded", function () {
    loadBooking()
    checkJwtExpiration(localStorage.getItem("jwt"))

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
        window.location.href = "login.html";
    } else {
        console.log('Token is still valid');
    }
}
function loadBooking() {
    let url = "http://localhost:8081/api/v1/booking-requests";
    let bookingTable = document.getElementById("booking");
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
            console.log(dataJson);
            dataJson.data.forEach(element => {
                bookingRequest += `<tr>
                <td>${element.id}</td>
                <td>${element.slot.id}</td>
                <td>${element.userInfo.id}</td>
                <td>${element.note}</td>
                <td>${element.startDate}</td>
                <td>${element.endDate}</td>
                <td>${element.createdDate}</td>
                <td>
                ${element.status == "Processing" ? `<select onchange=" accept(${element.id}, this.value)">
                <option value="Processing" ${element.status == "Processing" ? "selected" : ""}>Processing</option>
                <option  value="Accept" >Accept</option>
                <option  value="Decline"  >Decline</option>    
           </select>` : `${ element.status == "Accept" ? `<a style="color: green; font-weight:900; font-size:18px"">Accept</a>` :   `<a style="color: red; font-weight:900; font-size:18px"">Decline</a>` }`} 
                
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
        updateBookingRequest(id, value)
    } else {
        setTimeout(loadBooking(), 500)


    }
}


function updateBookingRequest(id, value) {
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

        .catch(error => {
            console.error('Error:', error);
        });
    setTimeout(loadBooking, 500);
}