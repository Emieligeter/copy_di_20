/**
 * This file contains all javascript functions regarding the list of SUMO files.
 */
function loadFiles() {
	/*var httpReq = new XMLHttpRequest();
	var sumoFiles = document.getElementById("sumoFiles");
	httpReq.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
		  var liElem = document.createElement("li");
			var name = "Loaded SUMO File";
			var date = "28/05/2020";
			var researcher = "John Doe";
			var tags = "wow, some tags";
		    liElem.innerHTML = "<a href=\"#\" class=\"list-group-item list-group-item-action flex-column align-items-start\">\n" +
			  	"<div class=\"d-flex w-100 justify-content-between\">\n" +
			  		"<h5 class=\"mb-1\">" + name + "</h5>\n" +
		   	 	"</div>\n" +
			  "<p class=\"mb-1\">Date: " + date + "</p>\n" +
			  "<p class=\"mb-1\">Researcher: " + researcher + "</p>\n" +
			  "<small>" + tags + "</small>\n" +
			  "</a>";
		    sumoFiles.appendChild(liElem);
	  }
	}
	httpReq.open("GET", "rest/simulation/simulationid", true);
	httpReq.send();*/
	var liElem = document.createElement("li");
	var name = "Loaded SUMO File";
	var date = "28/05/2020";
	var researcher = "John Doe";
	var tags = "traffic, car, numbers, data";
    liElem.innerHTML = "<a href=\"#\" class=\"list-group-item list-group-item-action flex-column align-items-start\">\n" +
	  	"<div class=\"d-flex w-100 justify-content-between\">\n" +
	  		"<h5 class=\"mb-1\">" + name + "</h5>\n" +
   	 	"</div>\n" +
	  "<p class=\"mb-1\">Date: " + date + "</p>\n" +
	  "<p class=\"mb-1\">Researcher: " + researcher + "</p>\n" +
	  "<small>" + tags + "</small>\n" +
	  "</a>";
    sumoFiles.appendChild(liElem);
}

function getFilteredFiles() {
  var input, filter, files, li, a, i, txtValue;
  input = document.getElementById('fileSearch');
  filter = input.value.toUpperCase();
  files = document.getElementById("sumoFiles");
  li = files.getElementsByTagName('li');
  // Loop through all list items, and hide those who don't match the search query
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

$("#sumoFiles").ready(function() {
    $('sumoFiles').click(function() {
    	// Select all list items 
        var files = $("sumoFiles"); 
        // Remove 'active' tag for all list items 
        for (let i = 0; i < files.length; i++) { 
            files[i].classList.remove("active"); 
        } 
        // Add 'active' tag for currently selected item 
        this.classList.add("active"); 
    });
});

/*$("#sumoFiles").click(function(){
	  alert("You have clicked a file");
	//Get the container element
	  var sumoList = document.getElementById("sumoFiles");

	  // Get all buttons with class="btn" inside the container
	  var files = sumoList.getElementsByTagName("li");

	  // Loop through the buttons and add the active class to the current/clicked button
	  for (var i = 0; i < files.length; i++) {
	    files[i].addEventListener("click", function() {
	      var current = document.getElementsByClassName("active");

	      // If there's no active class
	      if (current.length > 0) {
	        current[0].className = current[0].className.replace(" active", "");
	      }

	      // Add the active class to the current/clicked button
	      this.className += " active";
	    });
	  }
	});*/