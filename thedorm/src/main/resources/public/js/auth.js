function signup() {
    url = "http://localhost:8081/api/v1/auth/register";
    username = document.querySelector('input[name="username"]').value;
    password = document.querySelector('input[name="password"]').value;
    re_password = document.querySelector('input[name="re_password"]').value;
    if(username != null && username != "" && password != null && password != "" && password == re_password){
        jsonData = { "username": username,
            "password": password};
            console.log(jsonData);
            console.log(JSON.stringify(jsonData));
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
                    localStorage.setItem('jwt', data.token);
                    console.log(data);
                }).catch(error => {
                    console.error('Error:', error);
                });
    }   
}

function login() {
    url = "http://localhost:8081/api/v1/auth/authenticate";
    username = document.querySelector('input[name="username"]').value;
    password = document.querySelector('input[name="password"]').value;
    if(username != null && username != "" && password != null && password != ""){
        jsonData = { "username": username,
            "password": password};
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
                }).catch(error => {
                    console.error('Error:', error);
                });
    }   
}

function logout(){
    localStorage.removeItem('jwt');
}




