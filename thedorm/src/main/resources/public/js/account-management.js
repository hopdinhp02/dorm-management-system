function searchUser(){
  let name = document.getElementById("name").value
  let role = document.getElementById("role").value  
  let isActive = document.getElementById("isActive").value
  let userTable = document.getElementById("userTable")
  let user = `` 
  let url = ""
  console.log(isActive);
   url = "http://localhost:8081/api/v1/user-infos/search-users?name="+name+"&role="+role+"&isActive="+isActive ;
   console.log(url);
  fetch(url, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem("jwt")}`
    }
})
    .then(response => response.json())
    .then(console.log(4))
    .then(jsonData => {
        jsonData.forEach(element => {
        user += ` 
    <tr>
    <td>${element.id}</td>
    <td><img src="/images/avata.jpg" style="width: 80px; height: 80px;" onclick="enlargeImage(this)" id="myImage"></td>
    <td><img src="/images/cccd.jpg" style="width: 150px; height: 80px;" onclick="enlargeImage(this)" id="myImage"></td>
    <td>${element.name}</td>
    <td>${element.email}</td>
    <td>${element.phone}</td>
    <td>${element.gender==true?"Male":"Female"}</td>
    <td>${element.role}</td>
    <td><select id="isActive" onchange="updateIsActive(${element.id},this.value)"> 
    <option ${element.active==true?"selected":""} value = "true">True</option>
    <option ${element.active==false?"selected":""} value = "false">False</option>
    </select>
    </td>

    </tr>`;
        userTable.innerHTML = user;
    })
    

    })
    .catch(error => {
        console.log(error);
    });
}


function enlargeImage(img) {
    var overlay = document.createElement("div");
    overlay.setAttribute("id", "overlay");
    overlay.setAttribute("onclick", "shrinkImage()");
    overlay.setAttribute("class", "floating-div");
    document.body.appendChild(overlay);
    
    var image = document.createElement("img");
    image.setAttribute("src", img.src);
    image.setAttribute("id", "enlarged-image");
    console.log(image);
    overlay.appendChild(image)
    console.log(overlay);
    
  }
  
  function shrinkImage() {
    var overlay = document.getElementById("overlay");
    var image = document.getElementById("enlarged-image");
    document.body.removeChild(overlay);
    document.body.removeChild(image);
  }


function updateIsActive(userId, value){
    if (confirm("Are you sure to update?")) {
        url = "http://localhost:8081/api/v1/user-infos/"+userId+"/active?is-active="+ value
    console.log(url);
    fetch(url, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
        
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .then(searchUser)
        .catch(error => {
            console.error('Error:', error);
        });
    }
}