/**
 * This file contains all functions regarding the html files.
 */

function loadFiles() {
	var httpReq = new XMLHttpRequest();
	  httpReq.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      updateFiles(this);
	    }
	  };
	  httpReq.open("GET", "cd_catalog.xml", true);
	  httpReq.send();
}

$("#sumoFiles").click(function(){
	  alert("You have clicked a file ");
	  //$(metadata).load(URL,data,callback);
	});



function updateFiles(xml) {
	  var i;
	  var xmlDoc = xml.responseXML;
	  var table="<tr><th>Artist</th><th>Title</th></tr>";
	  var x = xmlDoc.getElementsByTagName("CD");
	  for (i = 0; i <x.length; i++) {
	    table += "<tr><td>" +
	    x[i].getElementsByTagName("ARTIST")[0].childNodes[0].nodeValue +
	    "</td><td>" +
	    x[i].getElementsByTagName("TITLE")[0].childNodes[0].nodeValue +
	    "</td></tr>";
	  }
	  document.getElementById("demo").innerHTML = table;
	}

function getFilteredFiles() {
  // Declare variables
  var input, filter, files, li, a, i, txtValue;
  input = document.getElementById('myInput');
  filter = input.value.toUpperCase();
  files = document.getElementById("sumoFiles");
  li = files.getElementsByTagName('li');

  // Loop through all list items, and hide those who don't match the search
	// query
  for (i = 0; i < li.length; i++) {
    a = li[i].getElementsByTagName("a")[0];
    txtValue = a.textContent || a.innerText;
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      li[i].style.display = "";
    } else {
      li[i].style.display = "none";
    }
  }
}

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

