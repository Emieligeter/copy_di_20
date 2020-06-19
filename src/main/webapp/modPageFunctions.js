/** This file contains all functions regarding the modification page. */

//Uploads a SUMO file to the database
$("#uploadFiles").submit(function(event){
  	event.preventDefault(); // prevent default action
    var files = $('#uploadFiles')[0];
    var fd = new FormData(files);
    //POST request
  	$.ajax({
  		url : 'rest/simulations/upload',
  		type: 'POST',
  		data: fd,
  		contentType: false, 
  	    processData: false,
  	    success : function(response){
  	    	$("#server-results").html(response); 
  	   }
    });
})

//Loads all distinct tags that exist in the database
function loadTags() {
	var url = "/sumo-dashboard/rest/tags";
	var tagList = document.getElementById("tagList");
	$.get(url, function(data, status){
		//Create a checkbox element for every tag
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
			//Add tag to form
			tagList.appendChild(div);
		}
	});
}
