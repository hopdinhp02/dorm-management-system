
loadDorm();


function loadDorm() {
let dormTable = document.getElementById("dorms");
    let url = "http://localhost:8081/api/v1/dorms";
    let dorm = ``;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            dataJson.data.forEach(element => {
                dorm += `<tr>
                <td onclick="loadDormById(${element.id})">${element.id}</td>
                <td>${element.name}</td>
                <td>${element.branch.id}</td>
                </tr>`;
                console.log(dorm)
                
            });
            dormTable.innerHTML = dorm;


        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function loadDormById(id) {
    let url = "http://localhost:8081/api/v1/dorms/" + id;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            
            let dormSelected = dataJson
            document.getElementById("name").value = dormSelected.name;
            document.getElementById("branch").value = dormSelected.branch;
            updateBTN = document.getElementById("update");
            deleteBTN = document.getElementById("delete");
            deleteBTN.onclick = function(){
                deleteDorm(id)
            };
            updateBTN.onclick = function(){
                updateDorm(id)
            };



        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function addDorm() {
    url = "http://localhost:8081/api/v1/dorms";
    dormName = document.getElementById("name").value;
    branchId = document.getElementById("branch").value;
    jsonData = { name: dormName, branch: {id: branchId}};
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
        .then(loadDorm)
        .catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadDorm, 50);
   
}


function updateDorm(id) {
    
    url = "http://localhost:8081/api/v1/dorms/" + id
    dormName = document.getElementById("name").value;
    branchId = document.getElementById("branch").value;
    
    jsonData = { name: dormName, branch: {id : branchId} };
    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .then(loadDorm)
        .catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadDorm, 50);
}


function deleteDorm(id) {
    url = "http://localhost:8081/api/v1/dorms/" + id;
    dormName = document.getElementById("name").value;
    branchId = document.getElementById("branch").value;
    
    jsonData = { name: dormName, branch: {id : branchId} };
    fetch(url, {
        method: "DELETE"
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .then(loadDorm)
        .catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadDorm, 50);
}
