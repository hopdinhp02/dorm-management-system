setInterval(function () {
    checkJwtExpiration(localStorage.getItem("jwt"));
  }, 10000);
  
  function checkJwtExpiration(token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = decodedToken.exp * 1000; // convert to milliseconds
  
    // Check if the token has expired
    if (Date.now() >= expirationTime) {
      alert('Token has expired');
      console.log('Token has expired.\n Please login again!');
      window.location.href = "/login.html";
    } else {
      console.log('Token is still valid');
    }
  }
function payment(){
    let amount = document.getElementById("amount").value
    url = "http://localhost:8081/api/v1/user-infos/topup" + `?amount=${amount}`;
    console.log(url);
    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        },
    })
        .then(response => response.json())
        .then(data => {
        
            if (data.data == true) {
                alert("Top up successfully")
            }else{
                alert("failed")
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}