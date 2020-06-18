
$("#login").submit(function( event ) {
	event.preventDefault();
	clearResponse()
	var username = $("#login #inputUsername").val().trim();
	var password = $("#login #inputPassword").val().trim();
	var credentials = {'username': username,
 			 			'password': password}
	
	var serverResp = $("#login #server-response");
	$.ajax({
  		url : 'rest/auth/login',
  		type: 'POST',
  		contentType: 'application/json',
  		data: JSON.stringify(credentials),
  	    success : function(response){ 	    	
  	    	sessionStorage.setItem('token', response);
  	    	sessionStorage.setItem('credentials',credentials);
  	    	location.href="dashboard.html";
  	   },
		error : function(response) {
			if(response.status == 403) {
				$("#loginPage #server-response.error").html("Username or password invalid");
			} else {
				$("#loginPage #server-response.error").html("Something went wrong");
			}
		}
    });
  });
 
//InputValidation for confirm password
$("form#createNew").on("input", function() {
($("input[name='pw2']").get(0).setCustomValidity($("input[name='pw1']").val() != $("input[name='pw2']").val() ? "Passwords do not match." : ""));
});

$("form#createNew").submit(function( event ) {
	event.preventDefault();
	clearResponse();
	var credentials = 
			{"username": $("form#createNew #inputUsername").val().trim(),
			"password" : $("form#createNew #inputPassword").val().trim(),
			"email"    : $("form#createNew #inputEmail").val().trim()};
	$.ajax({
  		url : 'rest/auth/createUser',
  		type: 'POST',
  		contentType: 'application/json',
  		data: JSON.stringify(credentials),
  	    success : function(response){
  	    	$("#createPage #server-response.success").html(response);
  	   },
  	   error : function(response) {
  		   $('#createPage #server-response.error').html(response.responseText);
  	   }
    });
  });

function clearResponse(){
	$("#createPage #server-response.success").html('');
	$('#createPage #server-response.error').html('');
}
 