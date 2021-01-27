var engageId; // ustawia się po kliknięciu na marker!!! jest to zwykle 'id' potworka

function engage(e) {
    fetch("engage?login=" + login + "&enemyId="  + engageId + "&lon=" + lng + "&lat=" + lat)
     .then(response => response.json())
     .then(data => {
         alert(data.message); //TODO
         document.querySelector('#interaction').style.visibility = "hidden";
         if (data.drop) {
             alert("An item dropped!"); //TODO
         }
         window[("enemyMarker" + engageId)].destroy();
     })
      .catch(err => {
        document.querySelector('#interaction').style.visibility = "hidden";
        console.error(err);
    });


}
