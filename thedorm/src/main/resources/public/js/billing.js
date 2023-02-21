document.addEventListener("DOMContentLoaded", function () {
    loadBilling()
});

function loadBilling() {
    let url = "http://localhost:8081/api/v1/billings";
    let billingTable = document.getElementById("billing");
    let billing = ``;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            console.log(dataJson);
            dataJson.data.forEach(element => {
                billing += `<tr>
                <td>${element.id}</td>
                <td>${element.userInfo.id}</td>
                <td>${element.type}</td>
                <td>${element.cost}</td>
                <td>${element.status}</td>
                <td>${element.createdDate}</td>
                <td>${element.deadlineDate}</td>
                <td>${element.payDate}</td>
                </tr>`;
               
            });

            billingTable.innerHTML = billing;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}