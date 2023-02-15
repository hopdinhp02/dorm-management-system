let branchDropDown = document.getElementById("branchs");

document.addEventListener("DOMContentLoaded", function () {
    loadbranch()
});

function loadbranch() {
    let url = "http://localhost:8081/api/v1/branchs";
    fetch(url)
        .then(response => response.json())
        .then(jsonData => {
            jsonData.data.forEach(element => {
                var option = document.createElement("option");
                option.text = element.name;
                option.value = element.id;
                branchDropDown.appendChild(option);
            });

        })
        .catch(error => {
            console.error('Error:', error);
        });
    }


    let dormDropDown = document.getElementById("dorms");

    

    function loaddorm() {
        dormDropDown.innerHTML = '';
        const selectElement = document.getElementById("branchs");
        const branchId = selectElement.value;
        console.log(branchId);
        let url = "http://localhost:8081/api/v1/branchs/"+branchId;
        fetch(url)
            .then(response => response.json())
            .then(jsonData => {
                jsonData.data.dorms.forEach(element => {
                    var option = document.createElement("option");
                    option.text = element.name;
                    option.value = element.id;
                    dormDropDown.append(option);
                });
    
            })
            .catch(error => {
                console.error('Error:', error);
            });
        } 