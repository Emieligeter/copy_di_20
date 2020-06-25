
$("#login").submit(function( event ) {
	event.preventDefault();
	clearResponse()
	var username = $("#login #inputUsername").val().trim();
	var password = $("#login #inputPassword").val().trim();
	var credentials = {"username": username,
 			 			"password": password}

	var serverResp = $("#login #server-response");
	$.ajax({
  		url : 'rest/auth/login',
  		type: 'POST',
  		contentType: 'application/json',
  		data: JSON.stringify(credentials),
  	    success : function(response){   	    	
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

function logout(){
	
	$.ajax({
		url: 'rest/auth/logout',
		type: 'POST',
		success: function(response) {
			location.href = "loginPage.html";
		}
	});
};
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
  		   if(response.status == 409) {
  		   $('#createPage #server-response.error').html(response.responseText);
  		   } else {
  			 $('#createPage #server-response.error').html("Something went wrong, try again later");
  		   }
  	   }
    });
  });

function clearResponse(){
	$("#createPage #server-response.success").html('');
	$('#createPage #server-response.error').html('');
}
 