const urlParams = new URLSearchParams(window.location.search);
data = urlParams.get('data');
loadMaintenanceToTable()
function loadMaintenanceToTable() {
    console.log(3);
    let maintenanceTable = document.getElementById("maintenance")
    maintenanceTable.innerHTML = ``
    let maintenance = ``
    let url = "http://localhost:8081/api/v1/facilities/" + data + "/facility-detail/maintenances";
    console.log(data);
    console.log(url);
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
                maintenance += ` 
        <tr>
        <td>${element.id}</td>
        <td>${element.price}</td>
        <td>${element.note}</td>
        <td>${element.fixDate}</td>
        </tr>`;
                maintenanceTable.innerHTML = maintenance;
            })
        })
        .catch(error => {
            console.log("error");
        });
}


function addMaintenanceFacility() {
    let url = "http://localhost:8081/api/v1/facilities/" + data + "/facility-detail/maintenances";
    maintenancePrice = document.getElementById("price").value;
    maintenanceNote = document.getElementById("note").value;
    jsonData = { price: maintenancePrice, note: maintenanceNote};
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
        .then(loadMaintenanceToTable)
        .catch(error => {
            console.error('Error:', error);
        });

}