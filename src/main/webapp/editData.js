/**
 * 
 */
$("#modifyMetadata").submit(function(event) {
event.preventDefault(); // prevent default action
    var url = "/rest/simulations/id/{id}";
    var body = {
    		"date": "2020-06-09",
    		"description": "description",
    		"name": "yes"
    }
  	$.ajax({
  		url : url,
  		type: 'PUT',
  		data: body,
  		contentType: false, 
  	    processData: false,
  	    success : function(response){
  	    	$("#server-results").html(response); 
  	   }
    });
}