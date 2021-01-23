function retrieveHeroInfo() {
    fetch('player?login=' + login)
            //gets the JSON out of the response
            .then(response => response.json())
            .then(logPlayer);
}
function logPlayer(data) {
    name = data.name;
    level = data.level;
    exp = data.exp;
    health = data.health;
    maxHealth = data.maxHealth;
    strength = data.strength;
    defense = data.defense;
    speed = data.speed;
    talentPoints = data.talentPoints;
    fieldOfView = data.fieldOfView;
    avatarLink = data.avatarLink;

    console.log(data);

    document.getElementById("level").innerHTML = level;
    document.getElementById("exp").innerHTML = exp;
    document.getElementById("learningPoints").innerHTML = talentPoints;
    document.getElementById("strength").innerHTML = strength;
    document.getElementById("health").innerHTML = health;
    document.getElementById("maxHealth").innerHTML = maxHealth;
    document.getElementById("defense").innerHTML = defense;
    document.getElementById("speed").innerHTML = speed;
    document.getElementById("avatarImg").setAttribute("src", avatarLink);


    document.getElementById("heroPanel").style.visibility = "visible";
}
function backFromHeroPanel(){
    document.getElementById("heroPanel").style.visibility = "hidden";
}
function spendHealth(){
    if (parseInt(document.getElementById("learningPoints").innerHTML) <= 0) return;

    arg = "health";
    fetch('player/spendTalentPoint?login=' + login + "&which=" + arg)
              .then(response => response.json())
              .then(refreshHealth);
}
function refreshHealth(data){val = data.health;document.getElementById("health").innerHTML = val; val = data.maxHealth;document.getElementById("maxHealth").innerHTML = val;
                               document.getElementById("learningPoints").innerHTML = document.getElementById("learningPoints").innerHTML -1;}
function spendDefense(){
    if (parseInt(document.getElementById("learningPoints").innerHTML) <= 0) {
        document.getElementById("learningPoints").innerHTML = 0;
        return;
    }

    arg = "defense";
    fetch('player/spendTalentPoint?login=' + login + "&which=" + arg)
              .then(response => response.json())
              .then( refreshDefense);
}
function refreshDefense(data){val = data.defense;document.getElementById("defense").innerHTML = val;
                                document.getElementById("learningPoints").innerHTML = document.getElementById("learningPoints").innerHTML -1;}
function spendStrength(){
    if (parseInt(document.getElementById("learningPoints").innerHTML) <= 0) {
        document.getElementById("learningPoints").innerHTML = 0;
        return;
    }
    arg = "strength";
    fetch('player/spendTalentPoint?login=' + login + "&which=" + arg)
              .then(response => response.json())
              .then( refreshStrength);
}
function refreshStrength(data){val = data.strength;document.getElementById("strength").innerHTML = val;
                                document.getElementById("learningPoints").innerHTML = document.getElementById("learningPoints").innerHTML -1;}
function spendSpeed(){
    if (parseInt(document.getElementById("learningPoints").innerHTML) <= 0) {
        document.getElementById("learningPoints").innerHTML = 0;
        return;
    }
    arg = "speed";
    fetch('player/spendTalentPoint?login=' + login + "&which=" + arg)
              .then(response => response.json())
              .then( refreshSpeed);
}
function refreshSpeed(data){val = data.speed;document.getElementById("speed").innerHTML = val;
                                document.getElementById("learningPoints").innerHTML = document.getElementById("learningPoints").innerHTML -1;}
