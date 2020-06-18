/* This file contains all javascript functions regarding editing metadata. */

//Updates metadata in the database
$("#modifyMetadata").submit(function(event) {
	event.preventDefault();
	console.log("I am here");
    var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + getSelectedID();
    var newMetadata = document.getElementById("modifyMetadata");
    //Body of the PUT request
    var body = "{\", \"name\": \"" + newMetadata.elements[0].value + 
    "\"date\": \"" + newMetadata.elements[1].value + 
    "\", \"description\": \"" + newMetadata.elements[3].value + 
    "\", \"researcher\": \"" + newMetadata.elements[2].value + "\"}";
    var xhr = new XMLHttpRequest();
    //PUT request to update the metadata in the database
    xhr.open("PUT", url);
    xhr.setRequestHeader('Content-type','application/json');
    console.log(body);
    xhr.onload = function () {
    	//document.getElementById("submitMetadata").setAttribute("data-toggle", "modal");
    	document.getElementById("submitDataModal").setAttribute("aria-hidden", false);    	
    	//data-toggle="modal" data-target="#submitDataModal"
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
		processTags(data.tags);
	});
	
}

function processTags(tags) {
	var tagList = tags.split(', ');
	//console.log(tagList);
	for (var i = 0; i < tagList.length; i++) {
		//make tag active, probably using id = tag
	}
}

$("#deleteSimButton").click(function() {
	event.preventDefault(); // prevent default action
	var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
	console.log(url);
	$.ajax({
		url: url,
		type: "DELETE",
		success: function(){
			document.getElementById("deletionModal").setAttribute("aria-hidden", true);
		}
	});
})
