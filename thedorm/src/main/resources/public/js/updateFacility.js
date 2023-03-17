const urlParams = new URLSearchParams(window.location.search);
let data = urlParams.get('data');
loadFacilityDetailToTable()
loadFacilityDetail()

function loadFacilityDetail() {
    console.log(3);
    let url = "http://localhost:8081/api/v1/facilities/facility-detail/" + data;
    fetch(url)
        .then(response => response.json())
        .then(dataJson => {

            let facilityDetailSelected = dataJson
            document.getElementById("codeProduct").value = facilityDetailSelected.data.codeProduct;
            document.getElementById("name").value = facilityDetailSelected.data.name;
            document.getElementById("price").value = facilityDetailSelected.data.price;
            document.getElementById("provider").value = facilityDetailSelected.data.provider;
            document.getElementById("expirationDate").value = facilityDetailSelected.data.expirationDate;
            document.getElementById("procudingDate").value = facilityDetailSelected.data.producingDate;
            document.getElementById("type").value = facilityDetailSelected.data.type;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateFacility() {
    console.log(data);
    url = "http://localhost:8081/api/v1/facilities/facility-detail/" + data
    codeProduct = document.getElementById("codeProduct").value == "" ? null : document.getElementById("codeProduct").value
    namee = document.getElementById("name").value == "" ? null : document.getElementById("name").value
    price = document.getElementById("price").value == "" ? null : document.getElementById("price").value
    provider = document.getElementById("provider").value == "" ? null : document.getElementById("provider").value
    expirationDate = document.getElementById("expirationDate").value == "" ? null : document.getElementById("expirationDate").value
    procudingDate = document.getElementById("procudingDate").value == "" ? null : document.getElementById("procudingDate").value
    type = document.getElementById("type").value == "" ? null : document.getElementById("type").value
    console.log(expirationDate);
    const expirationdate = new Date(expirationDate);
    const expirationyear = expirationdate.getFullYear();
    const expirationmonth = expirationdate.getMonth() + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
    const expirationday = expirationdate.getDate();
    const expirationhours = expirationdate.getHours();
    const expirationminutes = expirationdate.getMinutes();
    const expirationseconds = expirationdate.getSeconds();
    const expirationFormattedDate = `${expirationyear}-${expirationmonth.toString().padStart(2, "0")}-${expirationday.toString().padStart(2, "0")} ${expirationhours.toString().padStart(2, "0")}:${expirationminutes.toString().padStart(2, "0")}:${expirationseconds.toString().padStart(2, "0")}`;


    const procudingdate = new Date(procudingDate);
    const procudingyear = procudingdate.getFullYear();
    const procudingmonth = procudingdate.getMonth() + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
    const procudingday = procudingdate.getDate();
    const procudinghours = procudingdate.getHours();
    const procudingminutes = procudingdate.getMinutes();
    const procudingseconds = procudingdate.getSeconds();
    const procudingFormattedDate = `${procudingyear}-${procudingmonth.toString().padStart(2, "0")}-${procudingday.toString().padStart(2, "0")} ${procudinghours.toString().padStart(2, "0")}:${procudingminutes.toString().padStart(2, "0")}:${procudingseconds.toString().padStart(2, "0")}`;
    jsonData = { codeProduct: codeProduct, name: namee, price: price, provider: provider, expirationDate: expirationFormattedDate, producingDate: procudingFormattedDate, type: type };
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
        .then(loadFacilityDetailToTable)
        .catch(error => {
            console.error('Error:', error);
        });

}



function loadFacilityDetailToTable() {
    console.log(3);
    let facilityTable = document.getElementById("facilityTable2")
    facilityTable.innerHTML = ``
    let facility = ``
    let url = "http://localhost:8081/api/v1/facilities/facility-detail/" + data;
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
            let element = jsonData.data
            console.log(jsonData);
            facility += ` 
        <tr>
        <td>${element.id}</td>
        <td>${element.name == null ? "" : element.name}</td>
        <td>${element.expirationDate == null ? "" : element.expirationDate}</td>
        <td>${element.codeProduct == null ? "" : element.codeProduct}</td>
        <td>${element.price == null ? "" : element.price}</td>
        <td>${element.producingDate == null ? "" : element.producingDate}</td>
        <td>${element.provider == null ? "" : element.provider}</td>
        <td>${element.type == null ? "" : element.type}</td>
        

        </tr>`;
            facilityTable.innerHTML = facility;
            console.log(5);

        })
        .catch(error => {
            console.log("error");
        });
}