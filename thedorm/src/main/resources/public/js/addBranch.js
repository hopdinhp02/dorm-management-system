
loadBranch();


let branchTable = document.getElementById("branchs");
function loadBranch() {
    let url = "http://localhost:8081/api/v1/branchs";
    let branch = ``;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            dataJson.data.forEach(element => {
                branch += `<tr>
                <td onclick="loadBranchById(${element.id})">${element.id}</td>
                <td>${element.name}</td>
                <td>${element.address}</td>
                <td>${element.phone}</td>
                <td>${element.type_id}</td>
                <td>${element.status}</td>
                </tr>`;
                console.log(branch)
                
            });
            branchTable.innerHTML = branch;


        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function addBranch() {
    url = "http://localhost:8081/api/v1/branchs";
    branchName = document.getElementById("name").value;
    branchAddress = document.getElementById("address").value;
    branchPhone = document.getElementById("phone").value;

    console.log(branchName);
    jsonData = { name: branchName, address: branchAddress, phone: branchPhone };
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
        setTimeout(loadBranch, 50);
   
}

function loadBranchById(id) {
    let url = "http://localhost:8081/api/v1/branchs/" + id;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {
            
            let branchSelected = dataJson
            document.getElementById("name").value = branchSelected.name;
            document.getElementById("address").value = branchSelected.address;
            document.getElementById("phone").value = branchSelected.phone;
            updateBTN = document.getElementById("update");
            deleteBTN = document.getElementById("delete");
            deleteBTN.onclick = function(){
                deleteBranch(id)
            };
            updateBTN.onclick = function(){
                updateBranch(id)
            };



        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function updateBranch(id) {
    
    url = "http://localhost:8081/api/v1/branchs/" + id
    branchName = document.getElementById("name").value
    branchAddress = document.getElementById("address").value
    branchPhone = document.getElementById("phone").value
    
    jsonData = { name: branchName, address: branchAddress, phone: branchPhone  };
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
        }).catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadBranch, 50);
}


function deleteBranch(id) {
    url = "http://localhost:8081/api/v1/branchs/" + id;
    fetch(url, {
        method: "DELETE"
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        }).catch(error => {
            console.error('Error:', error);
        });
        setTimeout(loadBranch, 50);
}
