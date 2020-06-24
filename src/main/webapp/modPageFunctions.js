/** This file contains all functions regarding the modification page. */

// Uploads a SUMO file to the database
$("#uploadFiles").submit(function(event){
  	event.preventDefault(); // prevent default action
    var files = $('#uploadFiles')[0];
    var fd = new FormData(files);
    $("#uploadResults").html("Server is processing, feel free to close this window.")
    //POST request
  	$.ajax({
  		url : 'rest/simulations/upload',
  		type: 'POST',
  		data: fd,
  		contentType: false, 
  	    processData: false,
  	    headers: {
  	    	"Authorization": "Bearer 12345"
		},
  	    success : function(response){
  	    	$("#uploadResults").html(response); 
  	    },
  		error : function(response){
  	    	$("#uploadResults").html("Error occured, code: " + response.status); 
  	    	console.error("Upload files response:\n" + JSON.stringify(response));
  	    }
    });
})

$('#resetUploadForm').click(function(event) {
	event.preventDefault();
	$("input[name='uploadFile']").val('');
})

//Loads all distinct tags that exist in the database
function loadTags() {
	$("#tagList").empty();
	var tagList = document.getElementById("tagList");
	$.ajax({
  		url : '/sumo-dashboard/rest/tags',
  		type: 'GET',
  	    headers: {
  	    	"Authorization": "Bearer 12345"
		},
  	    success : function(data){
  	    	// Create a checkbox element and corresponding label for every tag
			for (var i = 0; i < data.length; i++) {
				var div = document.createElement("div");
				div.class = "form-check";
				var checkBox = document.createElement("input");
				checkBox.class = "form-check-input";
				checkBox.type = "checkbox";
				checkBox.id = data[i];
				checkBox.name = "tags";
				checkBox.value = data[i];
				checkBox.disabled = true;
				var tag = document.createElement("label");
				tag.class = "form-check-label";
				tag.for = data[i];
				tag.innerHTML = data[i];
				div.appendChild(checkBox);
				div.appendChild(tag);
				// Add tag to form
				tagList.appendChild(div);
			}
  	    },
  		error : function(response){
  			alert("Error occured when receiving tags, status: " + response.status);
			console.error("Load tags response:\n" + JSON.stringify(response));
  			if(response.status == 401) location.href="loginPage.html"

  	    }
    });
}

// Reloads the list of files, so the updated metadata is shown
$("#closeSubmitData").click(function() {
	var id = getSelectedID();
	$('#sumoFiles').empty();
	loadFiles();
	console.log("ul li[id='" + id + "']");
	$("ul li[id='" + id + "']").children().click();
})

//Shows input to create a new tag when new tag button is clicked
$("#newTagButton").click(function() {
	if ($("#newTagButton").html() === "+ New tag") {
		$("#newTag").toggle();
		$("#newTagButton").html("Create");
	} else {
		createTag();
	}
})

