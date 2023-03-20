document.addEventListener("DOMContentLoaded", function () {
    loadBilling()
});


function loadBilling() {
    let url = "http://localhost:8081/api/v1/billings/resident";
    let billingTable = document.getElementById("billing");
    let billing = ``;
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
            dataJson.data.forEach(element => {
                billing += `<tr >
                <td>${element.id}</td>
                <td>${element.userInfo.id}</td>
                <td>${element.type}</td>
                <td>${element.cost}</td>
                <td>${element.status}</td>
                <td>${element.createdDate}</td>
                <td>${element.deadlineDate}</td>
                <td>${element.payDate}</td>
                <td>${element.status == "Unpaid" ? ` <button class="btn btn-primary" onclick="checkBallance(${element.id})">Pay</button>`:""}</td>
                </tr>`;
               
            });

            billingTable.innerHTML = billing;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function checkBallance(billId){
    console.log(1);
    url = "http://localhost:8081/api/v1/user-infos/check-balane-to-pay-bill";
    jsonData = { id: billId };
    fetch(url,
        {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("jwt")}`
            },
            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {
            console.log(data.data);
            if (data.data == true && confirm("Are you sure pay bill")) {
                payBill(billId);
            } else {
                alert("You don't have enough money to pay!!")
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function  payBill(billId) {
    console.log(1);
    url = "http://localhost:8081/api/v1/user-infos/pay-bill"
    jsonData = {id: billId };
    console.log(jsonData);
    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .then(loadBilling)
        .catch(error => {
            console.error('Error:', error);
        });
}
