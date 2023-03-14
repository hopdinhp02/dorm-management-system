const urlParams = new URLSearchParams(window.location.search);
let data = urlParams.get('data');
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
    maintenanceFixDate = document.getElementById("fixDate").value;
    const date = new Date(maintenanceFixDate);
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    const formattedDate = `${year}-${month.toString().padStart(2, "0")}-${day.toString().padStart(2, "0")} ${hours.toString().padStart(2, "0")}:${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
    console.log(formattedDate);
    jsonData = { price: maintenancePrice, note: maintenanceNote, fixDate: formattedDate };
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
        .then(loadMaintenanceToTable)
        .catch(error => {
            console.error('Error:', error);
        });

}