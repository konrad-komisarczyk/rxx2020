
var login = "user5";

function retrieveMenu() {
    console.log('retrieveMenu');

    fetch('player?login=' + login)
        //gets the JSON out of the response
        .then(response => response.json())
        .then(showMenu);
}

function showMenu(data) {
    document.getElementById("menuPanel").style.visibility = "visible";
}

function hideMenu() {
    document.getElementById("menuPanel").style.visibility = "hidden";
}

function changeAvatar() {
    var link = document.getElementById("avatarLink").value;

    fetch('/player/setAvatar', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
    },
    body: JSON.stringify({
    link: link
    })}).
    then(() => {fetch("/player?login=" + login).then(response => response.json())
                            .then(data => {
                            var size = new OpenLayers.Size(80, 80);
                            var offset = new OpenLayers.Pixel(-(size.w / 2), -(size.h / 2));
                            icon = new OpenLayers.Icon(data.avatarLink, size, offset);
                            actualizeMap();
                            });});
    document.getElementById("avatarLink").value = "";

}