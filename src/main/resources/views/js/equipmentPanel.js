var weapon;
var armor;
var backpack = [];

function backFromEquipmentPanel(){
    document.getElementById("equipmentPanel").style.visibility = "hidden";
}

function retrieveEquipment(){


    fetch('backpack?login=' + login)
                //gets the JSON out of the response
                .then(response => response.json())
                .then(logEquipment);

    document.getElementById("equipmentPanel").style.visibility = "visible";
    //document.getElementById("equipment").style.background = "chartreuse";
}

function logEquipment(data){
    console.log(data);
    console.log(data[0]);

    for (let i = 0; i < data.length; i++) {
        let position = data[i].position;
        let itemIdentifier = data[i].itemIdentifier;
        // weapon
        if(position === -1){
            imgPath = "weapon" + itemIdentifier;
            itemClass = "weapon_" + itemIdentifier;
            document.getElementById("weaponSlot").innerHTML = '<img class="'+itemClass+'" id="weaponImage" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';
        }
        // armor
        if(position === -2){
            imgPath = "armor"+ itemIdentifier;
            itemClass = "armor_" + itemIdentifier;
            document.getElementById("armorSlot").innerHTML = '<img class="'+itemClass+'" id="armorImage" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';


        }
        // backpack item
        if(position > 0 && position <= 20){
            imgPath = data[i].type + itemIdentifier;
            itemClass = data[i].type + "_" + itemIdentifier;
            document.getElementById("backpack" + position).innerHTML = '<img class = "'+itemClass+'" id="backpackImage'+position+'" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';
        }
    }
}

function itemInfoDisplay(ev) {
    var targetId = ev.target.id;

    var position = reduceFromWhich(targetId);
    if(position === "w"){
        position = -1;
    }
    if(position === "a"){
        position = -2;
    }



    fetch('backpack/itemDetails/?login=' + login + '&position=' + position)
                  .then(response => response.json())
                  .then(addItemDetails)
                  .catch(err => {console.error(err);
                         });
        function addItemDetails(data){
            document.getElementById("itemInfoName").innerHTML = data.name;
            document.getElementById("itemInfoDescription").innerHTML = data.description;
            document.getElementById("itemInfo").style.visibility = "visible";
        }
}

function hideItemInfo() {
    document.getElementById("itemInfo").style.visibility = "hidden";

}