var items = [];
function retrieveItems() {
    fetch('items')
            .then(response => response.json())
            .then(logItems);
}
function logItems(data) {
    items = [];
    for( var i = 0; i < data.length; i++){
            items.push([data[i].id, data[i].name, data[i].type, data[i].damage,
                        data[i].protection, data[i].healing, data[i].value])
    }
}