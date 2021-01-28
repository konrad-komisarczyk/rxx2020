var engageId; // ustawia się po kliknięciu na marker!!! jest to zwykle 'id' potworka

function engage(e) {
    fetch("engage?login=" + login + "&enemyId="  + engageId + "&lon=" + lng + "&lat=" + lat)
     .then(response => response.json())
     .then(data => {
         document.querySelector('#interaction').style.visibility = "hidden";

         document.getElementById("reportText").innerText = data.message;
         document.getElementById("combatReportPanel").style.visibility = "visible";

         if (data.drop) {
             document.getElementById("itemDroppedInfo").innerText = "Examining enemy's corpse you found an useful item. Check your inventory.";
         }
         window[("enemyMarker" + engageId)].destroy();
     })
      .catch(err => {
        document.querySelector('#interaction').style.visibility = "hidden";
        console.error(err);
    });


}

function hideCombatReport() {
    document.getElementById("combatReportPanel").style.visibility = "hidden";
}