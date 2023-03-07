document.addEventListener("DOMContentLoaded", function () {
    loadFacilityDetail()
    checkJwtExpiration(localStorage.getItem("jwt"))
    
});
setInterval(function(){
    checkJwtExpiration(localStorage.getItem("jwt"));
}, 10000); 
function checkJwtExpiration(token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = decodedToken.exp * 1000; // convert to milliseconds
  
    // Check if the token has expired
    if (Date.now() >= expirationTime) {
        alert('Token has expired');
      console.log('Token has expired.\n Please login again!');
      window.location.href="login.html";
    } else {
      console.log('Token is still valid');
    }
  }

  function loadFacilityDetail() {
    let url = "http://localhost:8081/api/v1/facilities";
    let facilityDetailTable = document.getElementById("facilityDetail");
    let FacilityDetail = ``;
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
                FacilityDetail += `<tr>
                <td>${element.facilityDetail.id}</td>
                <td>${element.facilityDetail.name}</td>
                <td>${element.facilityDetail.expirationDate}</td>
                <td>${element.facilityDetail.codeProduct}</td>
                <td>${element.facilityDetail.price}</td>
                <td>${element.facilityDetail.producingDate}</td>
                <td>${element.facilityDetail.provider}</td>
                <td>${element.facilityDetail.type}</td>
                <td>
                <select onchange=" accept(${element.id}, this.value)">
                     <option>${element.facilityDetail.status}</option>
                     <option value="good" ${element.facilityDetail.status == "good" ? "selected" : ""}>good</option>
                     <option value="broken"  ${element.facilityDetail.status == "broken" ? "selected" : ""}>broken</option>
                     <option value="irreparable"  ${element.facilityDetail.status == "irreparable" ? "selected" : ""}>irreparable</option>
                </select>
                </td>
                </tr>`;
                
            });

            facilityDetailTable.innerHTML = FacilityDetail;

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateFacilityDetail(id, value) {
    url = "http://localhost:8081/api/v1/facilities/"+ id  +"/facility-detail " 
    let facilityDetailStatus = value
    jsonData = { facilityDetail: { status: facilityDetailStatus }};
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
        .catch(error => {
            console.error('Error:', error);
        });
    setTimeout(loadFacilityDetail, 500);
}

function accept(id, value) {

    if (confirm("The value has changed to: " + value)) {
        updateFacilityDetail(id, value)
    } else {
        setTimeout(loadFacilityDetail(), 500)


    }
}