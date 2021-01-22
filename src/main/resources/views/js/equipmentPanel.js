var weapon;
var armor;
var backpack = [];
var money; // ? w equipment

function backFromEquipmentPanel(){
    document.getElementById("equipmentPanel").style.visibility = "hidden";
}

function retrieveEquipment(){


    fetch('player')
                //gets the JSON out of the response
                .then(response => response.json())
                .then(logEquipment);

    document.getElementById("equipmentPanel").style.visibility = "visible";
    document.getElementById("equipment").style.background = "chartreuse";
}

function logEquipment(data){
    weapon = data.weapon;
    armor = data.armor;
    backpack = data.backpack;
    money = data.money;

    if(typeof(weapon) !== "undefined"){
        imgPath = "weapon"+ weapon.itemIdentifier;
        itemClass = weapon.type + "_" + weapon.itemIdentifier;
        document.getElementById("weaponSlot").innerHTML = '<img class="'+itemClass+'" id="weaponImage" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';
    }
    if(typeof(armor) !== "undefined"){
            imgPath = "armor"+ armor.itemIdentifier;
            itemClass = armor.type + "_" + armor.itemIdentifier;
            document.getElementById("armorSlot").innerHTML = '<img class="'+itemClass+'" id="armorImage" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';

        }

    if(typeof(backpack) !== "undefined" && backpack !== null){
        for( var i = 0; i < backpack.length; i++){
                    if(typeof(backpack[i]) !== "undefined" && backpack[i] !== null){
                       imgPath = backpack[i].type + backpack[i].itemIdentifier;
                       itemClass = backpack[i].type + "_" + backpack[i].itemIdentifier;
                       document.getElementById("backpack" + i).innerHTML = '<img class = "'+itemClass+'" id="backpackImage'+i+'" src="img\\'+imgPath+'.png" draggable=true height=70 width=70 ondragstart="drag(event)" onClick="itemInfoDisplay(event)" style="display:block;">';
                    }
            }
    }
}

function itemInfoDisplay(ev) {
    var targetId = ev.target.id;
    fetch('player/getItemDetails/' + reduceFromWhich(targetId))
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