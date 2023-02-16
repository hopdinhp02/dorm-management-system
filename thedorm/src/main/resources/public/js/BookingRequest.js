
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