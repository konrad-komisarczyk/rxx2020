var lng = 21.012420, lat = 52.220190;

var zoom = 18;
var map, lngLat;
var playerMarker;
var icon;

var markers;

// var playerLat, playerLng;
function success(position) {
    if (!document.getElementById('movingModeCheckbox').checked) {
        lat  = position.coords.latitude; // change to playerLat = to get test location
        lng = position.coords.longitude; // change to playerLng = to get test location
    }
    actualizeMap();
    reloadEnemies();
}

function error() {
    status.textContent = 'Unable to retrieve your location';
}

function getCurrentPosition() {
    window.navigator.geolocation
        .getCurrentPosition(success, error);
}

if (window.navigator.geolocation) {
    getCurrentPosition();

    map = new OpenLayers.Map({
        div: "mapdiv",
        controls: [] // makes map unzoomable and unprzesuwalne
    });
    map.addLayer(new OpenLayers.Layer.OSM());

    lngLat = new OpenLayers.LonLat(lng, lat) // PW gmach
        .transform(
            new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
            map.getProjectionObject() // to Spherical Mercator Projection
        );

    markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);

    map.setCenter(lngLat, zoom);

    var size = new OpenLayers.Size(80, 80);
    var offset = new OpenLayers.Pixel(-(size.w / 2), -(size.h / 2));
    fetch("/player?login=" + login)
        .then(response => response.json())
    .then(data => {
        icon = new OpenLayers.Icon(data.avatarLink, size, offset);
                    playerMarker = new OpenLayers.Marker(lngLat, icon);
                    playerMarker.events.register("click", playerMarker, function() {
                            retrieveHeroInfo();
                    });
                    markers.addMarker(playerMarker);

    });

}

gettingPositionInterval = setInterval(getCurrentPosition, 2000); // take location every 2 seconds

function doActionIfEnter(e){
    if(document.getElementById("cheatInput").value === "iamtester"){
        document.getElementById("cheatInputBox").hidden = true;
        document.getElementById("movingMode").hidden = false;
    }
}

function actualizeMap() {
    lngLat = new OpenLayers.LonLat(lng, lat)
        .transform(
            new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
            map.getProjectionObject() // to Spherical Mercator Projection
        );
    map.setCenter(lngLat, zoom);
    markers.removeMarker(playerMarker);
    playerMarker = new OpenLayers.Marker(lngLat, icon);
    markers.addMarker(playerMarker);
}

function goLeft(){
    lng = lng - 0.0005;
    actualizeMap();
}
function goRight(){
    lng = lng + 0.0005;
    actualizeMap();
}
function goUp(){
    lat = lat + 0.0002;
    actualizeMap();
}
function goDown(){
    lat = lat - 0.0002;
    actualizeMap();
}

document.onkeydown = checkKey;

function checkKey(e) {
    e = e || window.event;
    if(document.getElementById('movingModeCheckbox').checked) {
        if (e.keyCode == '38') {
            goUp();
        } else if (e.keyCode == '40') {
            goDown();
        } else if (e.keyCode == '37') {
            goLeft();
        } else if (e.keyCode == '39') {
            goRight();
        }
    }
}

document.addEventListener( "DOMContentLoaded", addListeners, false );
document.addEventListener( "DOMContentLoaded", reloadEnemies, false );
document.addEventListener( "DOMContentLoaded", retrieveItems, false );
function addListeners() {
    document.getElementById('hero').addEventListener("click", retrieveHeroInfo, false);
    document.getElementById('menu').addEventListener("click", retrieveMenu, false);
    document.getElementById('equipment').addEventListener("click", retrieveEquipment, false);
    document.getElementById('interactionConfirm').addEventListener("click", engage, false);

    // buttony heroPanel
    document.getElementById('increaseHealth').addEventListener("click", spendHealth, false);
    document.getElementById('increaseStrength').addEventListener("click", spendStrength, false);
    document.getElementById('increaseDefense').addEventListener("click", spendDefense, false);
    document.getElementById('increaseSpeed').addEventListener("click", spendSpeed, false);
    document.getElementById('backFromHeroPanel').addEventListener("click", backFromHeroPanel, false);
    document.getElementById('backFromEquipmentPanel').addEventListener("click", backFromEquipmentPanel, false);
}
