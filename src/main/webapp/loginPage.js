
$("#credentials").submit(function( event ) {
	event.preventDefault();
	var credentials = "inputUsername=" + $("#inputUsername").val().trim() + 
	"&inputPassword=" + $("#inputPassword").val().trim();
	var credentials = "inputUsername=Username&inputPassword=Password";
	$.ajax({
  		url : 'rest/auth/login',
  		type: 'POST',
  		contentType: 'application/x-www-form-urlencoded',
  		data: credentials,
  	    success : function(response){
  	    	sessionStorage.setItem('token', response);
  	    	//location.href="dashboard.html";
  	   }
    });
  });
 
//InputValidation for confirm password
$("form#createNew").on("input", function() {
($("input[name='pw2']").get(0).setCustomValidity($("input[name='pw1']").val() != $("input[name='pw2']").val() ? "Passwords do not match." : ""));
});

$("form#createNew").submit(function( event ) {
	event.preventDefault();
	var credentials = 
			"username=" + $("form#createNew #inputUsername").val().trim() +
			"&password=" + $("form#createNew #inputPassword").val().trim() +
			"&email="    + $("form#createNew #inputEmail").val().trim();
	$.ajax({
  		url : 'rest/auth/createUser',
  		type: 'POST',
  		contentType: 'application/x-www-form-urlencoded',
  		data: credentials,
  	    success : function(response){
  	    	alert(response);
  	    	$("#server-response").html(response);
  	    	//location.href="dashboard.html";
  	   }
    });
  });
 