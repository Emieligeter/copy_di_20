/**
 * 
 */
$("#modifyMetadata").click(function() {
	event.preventDefault();
    var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/43655";
    var newMetadata = document.getElementById("modifyMetadata");
    var body = "{\"date\": \"" + newMetadata.elements[1].value + 
    "\", \"description\": \"" + newMetadata.elements[3].value + 
    "\", \"name\": \"" + newMetadata.elements[0].value + 
    "\", \"researcher\": \"" + newMetadata.elements[2].value + "\"}";
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", url);
    xhr.setRequestHeader('Content-type','application/json');
    console.log(body);
    xhr.onload = function () {
    	console.log(this.responseText);
    }
    xhr.send(body);
});

