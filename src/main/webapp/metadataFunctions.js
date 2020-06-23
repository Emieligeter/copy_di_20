/* This file contains all javascript functions regarding editing metadata. */

//Updates metadata in the database
$("#modifyMetadata").submit(function(event) {
	event.preventDefault();
    var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + getSelectedID();
    var newMetadata = document.getElementById("modifyMetadata");
    //Retrieve all tags that have a checked checkbox
    var tags = "";
    $('input:checkbox:checked').each(function () {
    	if (tags === "") {
    		tags += $(this).attr('id')
    	} else {
    		tags += ", " + $(this).attr('id');
    	}
    });;
    console.log(tags);
    //Body of the PUT request
    var body = "{\"name\": \"" + newMetadata.elements[0].value + 
    "\", \"date\": \"" + newMetadata.elements[1].value + 
    "\", \"description\": \"" + newMetadata.elements[3].value + 
    "\", \"researcher\": \"" + newMetadata.elements[2].value + 
    "\", \"tags\": \"" + tags + "\"}";
    console.log(body);
    $.ajax({
  		url : url,
  		type: 'PUT',
  		data: body,
  	    headers: {
  	    	"Authorization": "Bearer 12345",
  	    	"Content-Type": "application/json"
		},
  	    success: function(response){
  	    	$("#updateResults").html("Updated metadata successfully!"); 
  	    },
  		error: function(response){
  	    	$("#updateResults").html("Error occured, code: " + response.status); 
  	    	console.error("Upload files response:\n" + JSON.stringify(response));
  	    }
    });
});

//When a file is clicked, its metadata will be displayed on the page
function fileClick(id) {
	$.ajax({
  		url : '/sumo-dashboard/rest/simulations/id/' + id,
  		type: 'GET',
  	    headers: {
  	    	"Authorization": "Bearer 12345"
		},
  	    success : function(data){
  	    	$("#newTitle").attr("value", data.name);
  			$("#newDate").attr("value", data.date);
  			var researcher = (data.researcher === undefined) ? "undefined" : data.researcher;
  			$("#newResearcher").attr("value", researcher);
  			$("#newDescription").html(data.description);
  			processTags(data.tags);
  	    },
  		error : function(response){
  			alert("Error occured when receiving simulation, code: " + response.status);
			console.error("Load simulation response:\n" + JSON.stringify(response));
  	    }
    });	
}

//Displays the tags of a simulation
function processTags(tags) {
	var checkboxes = $('input[type=checkbox]').get();
	var tagList = tags.split(', ');
	for(var i = 0; i < checkboxes.length; i++) {
	    checkboxes[i].disabled = false;
	    if (tagList.includes(checkboxes[i].id)) {
	    	$("#" + checkboxes[i].id).prop('checked', true);
	    } else {
	    	$("#" + checkboxes[i].id).prop('checked', false);
	    }
	}
}

//Deletes the currently selected simulation file from the database
$("#deleteSimButton").click(function() {
	event.preventDefault(); // prevent default action
	var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
	$.ajax({
		url: url,
		type: "DELETE",
		headers: {
  	    	"Authorization": "Bearer 12345"
		},
		success: function(){
			location.reload();
			//document.getElementById("deletionModal").setAttribute("aria-hidden", true);
			//alert("Deleted succesfully");
		},
  		error : function(response){
  			alert("Error occured when deleting simulation, code: " + response.status);
			console.error("Delete simulation response:\n" + JSON.stringify(response));
  	    }
	});
})
