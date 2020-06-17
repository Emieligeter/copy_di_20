/**
 * This file contains all functions regarding the modification page.
 */

$("#uploadFiles").submit(function(event){
  	event.preventDefault(); // prevent default action
  	
    var files = $('#uploadFiles')[0];
    var fd = new FormData(files);
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

function loadTags() {
	var url = "/sumo-dashboard/rest/tags";
	var tagList = document.getElementById("tagList");
	$.get(url, function(data, status){
		for (var i = 0; i < data.length; i++) {
			var checkBox = document.createElement("input");
			checkBox.type = "checkbox";
			checkBox.id = data[i];
			checkBox.name = "tags";
			checkBox.value = data[i];
			var tag = document.createElement("label");
			tag.for = data[i];
			tag.innerHTML = " " + data[i];
			/*var tag = "<input type=\"checkbox\" id=\"" + data[i] + "\" name=\"tags\" value=\"" + data[i] + "\">\n" + 
  			"<label for=\"" + data[i] + "\"> " + data[i] + "</label><br>";*/
			tagList.appendChild(checkBox);
			tagList.appendChild(tag);
			tagList.appendChild(document.createElement("br"));
		}
	});
}
