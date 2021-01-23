
function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("text"); //

  var dataClass = document.getElementById(data).className;
  var targetId = ev.target.id; // backpack{0-19} lub armorSlot lub weaponSlot
  var targetClass = ev.target.className;
  console.log(data); // backpackImage{0-19} lub armorImage lub weaponImage
  console.log(targetId);

  if(targetId === "equipmentTrash"){
      let which = reduceFromWhich(data)
      if(which === "w"){
          return;
          //which = -1;
      }
      if(which === "a"){
          return;
          //which = -2;
      }
    fetch('backpack/deleteItem/?login=' + login +'&pos=' + which, {method: 'DELETE'})
                  .then(() => {deleteTrashItem();})
                  .catch(err => {console.error(err);
                         });
        function deleteTrashItem(){
            document.getElementById(data).remove();
        }
        return;

      }

  if(dataClass[0] === "c" && (targetClass[0] === "w" || targetClass[0] === "a" || targetClass === "biggerCell")){
      let which = reduceFromWhich(data)
      if(which === "w"){
          which = -1;
      }
      if(which === "a"){
          which = -2;
      }
      fetch('backpack/consumeItem/?login=' + login +'&pos=' + which)
              .then(() => {deleteConsumedItem();})
              .catch(err => {console.error(err);
                     });
    function deleteConsumedItem(){
        document.getElementById(data).remove();
    }
    return;

  }

  //console.log(ev.target.innerHTML)
  if(ev.target.innerHTML !== "" && ev.target.innerHTML[0] !== " ") return; // omijanie zajetych slotow
  if(targetClass[0] === "w" && dataClass[0] !== "w") return; // totally wrong replacement :)
  if(targetClass[0] === "a" && dataClass[0] !== "a") return; // totally wrong replacement :)
  if(dataClass[0] === "c" && targetClass == "biggerCell") return;
  if(dataClass[0] === "w" && targetId === "armorSlot") return;
  if(dataClass[0] === "a" && targetId === "weaponSlot") return;

  console.log(ev.target.innerHTML + " <- passed with this inner HTML target");
  // dataId & targetId to string query
  fromWhich = reduceFromWhich(data);
  toWhich = reduceToWhich(targetId);
    if(fromWhich === "w"){
        fromWhich = -1;
    }
    if(fromWhich === "a"){
        fromWhich = -2;
    }
    if(toWhich === "w"){
        toWhich = -1;
    }
    if(toWhich === "a"){
        toWhich = -2;
    }

  fetch('backpack/moveItem/?login='+login+'&posBefore=' + fromWhich + "&posAfter=" + toWhich)
          .then(() => {switchPlaces();})
          .catch(err => {console.error(err);
                 });

  // zmien id z weaponImage na backpackImage!!!
  function switchPlaces(){
      console.log("DATAAA2: " + data);
  if(targetId[0] === "a"){
    document.getElementById(data).id = "armorImage";
    data = "armorImage";
  }
  if(targetId[0] === "w"){
      document.getElementById(data).id = "weaponImage";
      data = "weaponImage";
  }
  if(targetId[0] === "b"){
      document.getElementById(data).id = "backpackImage" + toWhich;
      data = "backpackImage" + toWhich;
  }

  ev.target.appendChild(document.getElementById(data));
}
}


function reduceFromWhich(text){
    if(text.length === 9) return text[8];
    if(text.length === 10) {if(text[8] !== "g") return (text[8] + text[9]);
                            else return "a";}
    if(text.length === 14) return text[13];
            if(text.length === 15) return (text[13] + text[14]);
    console.log("cos sie zepsulo 1");
    console.log(text.length+"---"+text);
    return text[0];
}

function reduceToWhich(text){
    if(text[0] === "b") {
        if(text.length === 14) return text[13];
        if(text.length === 15) return (text[13] + text[14]);
        if(text.length === 9) return text[8];
        if(text.length === 10) return text[8] + text[9];

    }
    console.log("cos sie zepsulo 2");
    console.log(text.length +"---"+text);
    return text[0];
}

function changeItemInTheList(query){
    fetch('player/moveItem/' + query)
        .then(response => response.json());


}