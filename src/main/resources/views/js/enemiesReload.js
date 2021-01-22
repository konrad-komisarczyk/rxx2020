
function reloadEnemies() {
    fetch('reload?login=' + login + "&lon=" + lng + "&lat=" + lat)
            //gets the JSON out of the response
            .then(response => response.json())
            .then(logEnemies);
}

function logEnemies(data) {
    markers.clearMarkers();
    playerMarker = new OpenLayers.Marker(lngLat, icon);
    markers.addMarker(playerMarker);

    for (var i = 0; i < data.length; i++) {
        var enemy = data[i];

        lonLat = new OpenLayers.LonLat(enemy.lon, enemy.lat)
            .transform(
                new OpenLayers.Projection("EPSG:4326"),
                map.getProjectionObject()
        );

        var enemyIcon = new OpenLayers.Icon(enemy.image, size, offset);

        window[("enemyMarker" + enemy.id)] = new OpenLayers.Marker(lonLat, enemyIcon);

        var module = {
            id: enemy.id,
            name: enemy.name,
            image: enemy.image,
            getX: function() {
                engageId = this.id;
                document.querySelector('#interaction').style.visibility = "visible";
                document.querySelector('#interactionImage').innerHTML = '<img src="' + this.image + '">';
                document.querySelector('#interactionTitle').innerHTML = this.name;
            }
        };

        window[("enemyMarker" + enemy.id)].events.register("click", window[("enemyMarker" + enemy.id)], module.getX.bind(module));
        markers.addMarker(eval("enemyMarker" + enemy.id));

    }
}


