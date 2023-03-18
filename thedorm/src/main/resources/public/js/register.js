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
                "Content-Type": "application/json",
                'Authorization': `Bearer ${localStorage.getItem("jwt")}`
            },

            body: JSON.stringify(jsonData)
        }
    )
        .then(respone => respone.json())
        .then(data => {

            console.log(data);
        })
        .then(alert("Register Successfully!!"))
        .catch(error => {
            console.error('Error:', error);
        });

}