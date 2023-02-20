document.addEventListener("DOMContentLoaded", function () {
    loadResidentHistory()
});

function loadResidentHistory() {
    let url = "http://localhost:8081/api/v1/resident-histories";
    let residentHistoryTable = document.getElementById("residentHistory");
    let residentHistory = ``;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            console.log(dataJson);
            dataJson.data.forEach(element => {
                residentHistory += `<tr>
                <td>${element.id}</td>
                <td>${element.checkinDate}</td>
                <td>${element.checkoutDate}</td>
                <td>${element.startDate}</td>
                <td>${element.endDate}</td>
                <td>${element.userInfo.id}</td>
                </tr>`;
               
            });

            residentHistoryTable.innerHTML = residentHistory;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}