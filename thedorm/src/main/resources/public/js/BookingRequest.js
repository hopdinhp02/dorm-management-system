
document.addEventListener("DOMContentLoaded", function () {
    loadBooking()
});

function loadBooking() {
    let url = "http://localhost:8081/api/v1/booking_Requests";
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            dataJson.data.forEach(element => {
                bookingRequest = `<tr>
                <td>${element.id}</td>
                <td>${element.note}</td>
                <td>${element.start_date}</td>
                <td>${element.end_date}</td>
                <td>${element.created_date}</td>
                <td>${element.status}</td>
                <td>${element.bed.id}</td>
                <td>${element.user_info.id}</td>
                </tr>`;
                console.log(bookingRequest)
                bookingTable.innerHTML += bookingRequest;
            });


        })
        .catch(error => {
            console.error('Error:', error);
        });
}



function addProduct() {
    url = "http://localhost:8080/api/v1/Products/insert";
    productName = document.getElementById("productName").value;
    jsonData = { productName: productName };
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
        }).catch(error => {
            console.error('Error:', error);
        });
}