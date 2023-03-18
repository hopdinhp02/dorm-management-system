loadMaintenance();

function loadMaintenance (){
    let maintenanceTable = document.getElementById("maintenance")
    let a =``
    let url = "http://localhost:8081/api/v1/maintenance";
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
                a += ` <td>${element.id}</td>
                <td>${element.facilityDetail.id}</td>
                <td>${element.price}</td>
                <td>${element.note}</td>
                <td>${element.fixDate}</td>
        
                `;
            });
            maintenanceTable.innerHTML = a;

        })
        .catch(error => {
            console.log(error);
        });

}