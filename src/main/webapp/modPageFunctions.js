/**
 * This file contains all functions regarding the modification page.
 */

$("#uploadFiles").submit(function(event){
  	event.preventDefault(); // prevent default action
  	
    var files = $('#uploadFiles')[0];
    var fd = new FormData(files);
    console.log(fd);
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

