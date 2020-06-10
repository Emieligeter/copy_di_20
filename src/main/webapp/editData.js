/**
 * 
 */
$("#modifyMetadata").click(function() {
	event.preventDefault(); // prevent default action
    var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/43655";
    //var body = {"date": "2020-06-10", "description": "Wow this description is so different", "name": "SUMO file checkk", "researcher": "Mr. Sir"};
    var body = "{\"date\": \"2020-06-10\", \"description\": \"Wow this description is so different\", \"name\": \"SUMO file checkk\", \"researcher\": \"Mr. Sir\"}";
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", url);
    xhr.setRequestHeader('Content-type','application/json');
    console.log(body);
    xhr.onload = function () {
    	console.log(this.responseText);
    	console.log("Successss!!!!!");
    }
    xhr.send(body);
  	/*$.ajax({
  		url: url,
  		type: 'PUT',
  		data: body,
  		contentType: "application/json",
  	    success : function(response){
  	    	console.log("Successful!!!!!");
  	   }
    });*/
});

/*
"date": document.getElementById("newDate").getAttribute("value"),
"description": document.getElementById("newDescription").innerHTML,
"name": document.getElementById("newName").getAttribute("value"),
"researcher": document.getElementById("newResearcher").getAttribute("value")
*/
//    var body = "{\"date\": \"2020-06-10\", \"description\": \"Wow this description is so different\", \"name\": \"SUMO file checkk\", \"researcher\": \"Mr. Sir\"}";
