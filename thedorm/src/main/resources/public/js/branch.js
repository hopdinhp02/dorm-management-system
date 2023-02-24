let branchTable = document.getElementById("branchTable");
function loadProduct() {
    let url = "http://localhost:8081/api/v1/branchs";
    fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
      })
        .then(response => response.json())
        .then(dataJson => {
            dataJson.data.forEach(element => {
                branch = `<tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.address}</td>
                <td>${element.phone}</td>
                <td>${element.type_id}</td>
                <td>${element.status}</td>
                </tr>`;
                console.log(branch)
                branchTable.innerHTML += branch;
            });


        })
        .catch(error => {
            console.error('Error:', error);
        });
}
loadProduct();
console.log(branchTable)
