var engageId; // ustawia się po kliknięciu na marker!!! jest to zwykle 'id' potworka

function engage(e) {
    fetch("engage?login=" + login + "&enemyId="  + engageId + "&lon=" + lng + "&lat=" + lat)
     .then(response => response.json())
     .then(data => {if(data === "DROP") {console.log("dropped");}/*document.getElementById("equipment").style.background = "gold";*/})// info o dropie
     .then(() => {
        document.querySelector('#interaction').style.visibility = "hidden";

        // // deleting from array
        //  for (var i = 0; i < enemies.length;i++) {
        //      if (enemies[i].id == engageId) {
        //          enemies.splice(i, 1);
        //          console.log("enemyMarker" + engageId);
        //          break;
        //      }
        //  }

         window[("enemyMarker" + engageId)].destroy();
      })
      .catch(err => {
        document.querySelector('#interaction').style.visibility = "hidden";
        console.error(err);
    });


}
