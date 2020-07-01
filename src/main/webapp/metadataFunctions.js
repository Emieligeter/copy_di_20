/* This file contains all javascript functions regarding editing metadata. */

//Updates metadata in the database
$("#modifyMetadata").submit(function(event) {
	event.preventDefault();
    var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
    var newMetadata = document.getElementById("modifyMetadata");
    //Retrieve all tags that have a checked checkbox
    var tags = "";
    $('input:checkbox:checked').each(function () {
    	//First tag in the list should not have a comma at the start
    	if (tags === "") {
    		tags += $(this).attr('id')
    	} else {
    		tags += ", " + $(this).attr('id');
    	}
    });
    //Body of the PUT request
    var body = "{\"name\": \"" + newMetadata.elements[0].value + 
    "\", \"date\": \"" + newMetadata.elements[1].value + 
    "\", \"description\": \"" + newMetadata.elements[3].value + 
    "\", \"researcher\": \"" + newMetadata.elements[2].value + 
    "\", \"tags\": \"" + tags + "\"}";
    $.ajax({
  		url : url,
  		type: 'PUT',
  		data: body,
  	    headers: {
  	    	"Content-Type": "application/json"
		},
  	    success: function(response){
  	    	$("#updateResults").html("Updated metadata successfully!"); 
  	    },
  		error: function(response){
  			if (response.status == 401) {
  				location.href = "loginPage.html";
  			}
  			else {
  				$("#updateResults").html("Error occured, code: " + response.status); 
  	  	    	console.error("Upload files response:\n" + JSON.stringify(response));
  			}
  	    }
    });
});

//When a file is clicked, its metadata will be displayed on the page
function fileClick(id) {
	$('#deleteSimButton').attr('disabled', false);
	$('#submitMetadata').attr('disabled', false);
	$('#resetMetadataForm').attr('disabled', false);
	$.ajax({
  		url : '/sumo-dashboard/rest/simulations/id/' + id,
  		type: 'GET',
  	    success : function(data){
  	    	document.getElementById('newTitle').value = data.name;
  	    	document.getElementById('newDate').value = data.date;
  			var researcher = (data.researcher === undefined) ? "undefined" : data.researcher;
  			document.getElementById('newResearcher').value = researcher;
  			$('#newDescription').empty();
  			document.getElementById('newDescription').innerHTML = data.description;
  			processTags(data.tags);
  			$("#updateResults").html("");
  	    },
  		error : function(response){
  			if (response.status == 401) {
  				location.href = "loginPage.html";
  			}
  			else {
	  			alert("Error occured when receiving simulation, code: " + response.status);
				console.error("Load simulation response:\n" + JSON.stringify(response));
  			}

		}
    });	
}

//Displays the tags of a simulation
function processTags(tags) {
	var checkboxes = $('input[type=checkbox]').get();
	var tagList;
	if (tags != null) {
		tagList = tags.split(', ');
	}
	else {
		tagList = [];
	}
	
	for(var i = 0; i < checkboxes.length; i++) {
	    checkboxes[i].disabled = false;
	    if (tagList.includes(checkboxes[i].id)) {
	    	$("#" + checkboxes[i].id).prop('checked', true);
	    } else {
	    	$("#" + checkboxes[i].id).prop('checked', false);
	    }
	}
}

//Creates a new tag in the database
function createTag() {
	var newTag = $("#newTag").val();
	//Empty tags or tags containing a quotation mark or apostrophe are not allowed
	if (newTag.includes('"') || newTag === "" || newTag.includes("'")) {
		$("#createTagSpan").html("Error: tag cannot be empty or contain \"");
	} else {
		$.ajax({
	  		url : 'rest/tags',
	  		method: 'POST',
	  		dataType: "text",
	  		data: newTag,
	  	    headers: {
	  	    	"Content-Type": "application/json"
			},
	  	    success : function(response){
	  	    	loadTags();
	  	    	//Hide input box for creating a tag
	  	    	$("#newTag").toggle();
	  			$("#newTagButton").html("+ New tag");
	  	    },
	  		error : function(response){
	  			if (response.status == 401) {
	  				location.href = "loginPage.html";
	  			}
	  			else {
		  	    	$("#updateResults").html("Error occured, code: " + response.status); 
		  	    	console.error("Create new tag response:\n" + JSON.stringify(response));
	  			}
	  	    }
	    });
	}
}

//Resets the values of the metadata form
$("#resetMetadataForm").click(function() {
	fileClick(getSelectedID());
})

//Deletes the currently selected simulation file from the database
$("#deleteSimButton").click(function() {
	event.preventDefault(); // prevent default action
	var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
	$.ajax({
		url: url,
		type: "DELETE",
		success: function(){
			location.reload();
		},
  		error : function(response){
  			if (response.status == 401) {
  				location.href = "loginPage.html";
  			}
  			else {
	  			alert("Error occured when deleting simulation, code: " + response.status);
				console.error("Delete simulation response:\n" + JSON.stringify(response));
  			}
  	    }
	});
})
