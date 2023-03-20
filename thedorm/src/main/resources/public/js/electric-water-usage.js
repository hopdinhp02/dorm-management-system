electricWaterUsage();

function electricWaterUsage() {
    let url = "http://localhost:8081/api/v1/elec-water-usages/all-electric-water-usage-of-resident";
    let electricWaterUsageTable = document.getElementById("electricWaterUsage");
    let electricWaterUsage = ``;
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
            // <th>ID</th>
            // <th>roomID</th>
            // <th>electricUsage</th>
            // <th>waterUsage</th>
            // <th>createdDate</th>
            // <th>electricEnd</th>
            // <th>electricStart</th>
            // <th>monthPay</th>
            // <th>waterStart</th>
            // <th>waterEnd</th>
            dataJson.data.forEach(element => {
                electricWaterUsage += `<tr>
                <td>${element.id}</td>
                <td>${element.room.id}</td>
                <td>${element.electricUsage}</td>
                <td>${element.waterUsage}</td>
                <td>${element.electricStart}</td>
                <td>${element.electricEnd}</td>                
                <td>${element.waterStart}</td>
                <td>${element.waterEnd}</td>
                <td>${element.createdDate}</td>
                <td>${element.monthPay}</td>
                </tr>`;
            });

            electricWaterUsageTable.innerHTML = electricWaterUsage;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}