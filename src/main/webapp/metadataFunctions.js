/**
 * 
 */
$("#modifyMetadata").submit(function() {
	event.preventDefault();
    var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + getSelectedID();
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

function fileClick(id) {
	var url = "/sumo-dashboard/rest/simulations/id/" + id;
	$.get(url, function(data, status){
		document.getElementById("newTitle").setAttribute("value", data.name);
		document.getElementById("newDate").setAttribute("value", data.date);
		var researcher = (data.researcher === undefined) ? "undefined" : data.researcher;
		document.getElementById("newResearcher").setAttribute("value", researcher);
		document.getElementById("newDescription").innerHTML = data.description;
	});
}

$("#deleteSimButton").click(function() {
	event.preventDefault(); // prevent default action
	
	var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
	console.log(url);
	$.ajax({
		url: url,
		type: "DELETE"
		success: function(){
			document.getElementById("deletionModal").setAttribute("aria-hidden", true);
		}
	});
})
