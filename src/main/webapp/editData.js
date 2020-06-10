/**
 * 
 */
$("#modifyMetadata").click(function() {
	event.preventDefault(); // prevent default action
    var url = "/rest/simulations/id/43655";
    var body = "{\"" +
    		"\"date\": \"2020-06-10\"," +
    		"\"description\": \"Wow this description is so different\"," +
    		"\"name\": \"SUMO file checkk\"," +
    		"\"researcher\": \"Mr. Sir\"" +
    "}";
  	$.ajax({
  		url: url,
  		type: 'PUT',
  		data: body,
  	    success : function(response){
  	    	console.log("Successful!!!!!");
  	   }
    });
});

/*
"date": document.getElementById("newDate").getAttribute("value"),
"description": document.getElementById("newDescription").innerHTML,
"name": document.getElementById("newName").getAttribute("value"),
"researcher": document.getElementById("newResearcher").getAttribute("value")
*/