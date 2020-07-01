//Logs the user out by calling logout endpoint, then redirects to the home page
$('#LogOut').click(function() {
	$.ajax({
		url: 'rest/auth/logout',
		type: 'POST',
		success: function(response) {
			location.href = "loginPage.html";
		},
		error: function(response){
			$("#uploadResults").html("Error occured, code: " + response.status); 
  	    	console.error("Upload files response:\n" + JSON.stringify(response));
		}
	});
});

//Show a nice welcome message to the user e.g. "Good afternoon, <username>"
var time = new Date().getHours();
if (time >= 4 && time < 12) {
	$('#topBorder h1').html("Good morning!");
}
else if (time >= 12 && time < 19) {
	$('#topBorder h1').html("Good afternoon!");
}
else {
	$('#topBorder h1').html("Good evening!");
}