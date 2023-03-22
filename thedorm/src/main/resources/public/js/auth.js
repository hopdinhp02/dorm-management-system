function register() {
    url = "http://localhost:8081/api/v1/auth/register";
    let username = document.getElementById("username").value
    let name = document.getElementById("name").value
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let phone = document.getElementById("phone").value
    let gender = document.querySelector('input[name="gender"]:checked').value;
    console.log(gender);
    jsonData = {username: username, password: password, email: email, phone:phone , gender: gender,name:name};
    console.log(jsonData);
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
            localStorage.removeItem('jwt');
            localStorage.setItem('jwt', data.token);
            console.log(data);
        })
        .then(moveToDashboard)
        .then(alert("Register Successfully!!"))
        .catch(error => {
            console.error('Error:', error);
        });

}

function login() {
    url = "http://localhost:8081/api/v1/auth/authenticate";
    username = document.querySelector('input[name="username"]').value;
    password = document.querySelector('input[name="password"]').value;
    if (username != null && username != "" && password != null && password != "") {
        jsonData = {
            "username": username,
            "password": password
        };
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
                localStorage.removeItem('jwt');
                localStorage.setItem('jwt', data.token);
                console.log(data);
            })
            .then(moveToDashboard)
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

function logout() {
    localStorage.removeItem('jwt');
    window.location.href = '/home.html'
}

function moveToDashboard() {
    url = "http://localhost:8081/api/v1/user-infos/role";
    fetch(url,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${localStorage.getItem("jwt")}`
            }
        }
    )
        .then(respone => respone.json())
        .then(jsonData => {
            role = jsonData.data;
            if(role =="USER"){
                window.location.href = "resident/index.html";
            }else if(role =="ADMIN"){
                window.location.href = "admin/index.html";
            }else if(role =="STAFF"){
                window.location.href = "staff/index.html";
            }else if(role =="GUARD"){
                window.location.href = "guard/index.html";
            }
        }).catch(error => {
            console.error('Error:', error);
        });
}





