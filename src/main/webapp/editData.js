/**
 * 
 */
$("").on('click', 'ul li a', function() {
event.preventDefault(); // prevent default action
  	
    var files = $('#uploadFiles')[0];
    var fd = new FormData(files);
    var url = "/rest/simulations/{id}";
  	$.ajax({
  		url : '',
  		type: 'PUT',
  		data: fd,
  		contentType: false, 
  	    processData: false,
  	    success : function(response){
  	    	$("#server-results").html(response); 
  	   }
    });
}