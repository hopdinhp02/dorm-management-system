loadNotification();

function loadNotification(){
    let notification = document.getElementById("notification")
    let a =``
    let url = "http://localhost:8081/api/v1/notification/resident";
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
                a += ` <li><a>${element.createdDate}</a>
                <a>${element.title}</a>&nbsp&nbsp&nbsp<button class="btn btn-primary" type="" onclick="loadContent(${element.id})">View Detail</button><br>
                <div>
                <p style="display: none;" id="${element.id}">${element.content}</p>
                </div>
                </li>
        `;
            });
            notification.innerHTML = a;

        })
        .catch(error => {
            console.log("error");
        });
}

function loadContent(id) {
    let p = document.getElementById(id)
    p.style.display = "block"
   
}