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
	var liElemTwo = document.createElement("li");
    liElemTwo.innerHTML = "<a href=\"#\" class=\"list-group-item list-group-item-action flex-column align-items-start\">\n" +
	  	"<div class=\"d-flex w-100 justify-content-between\">\n" +
	  		"<h5 class=\"mb-1\">" + name + "</h5>\n" +
   	 	"</div>\n" +
	  "<p class=\"mb-1\">Date: " + date + "</p>\n" +
	  "<p class=\"mb-1\">Researcher: " + researcher + "</p>\n" +
	  "<small>" + tags + "</small>\n" +
	  "</a>";
    sumoFiles.appendChild(liElem);
    sumoFiles.appendChild(liElemTwo);
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

$(document).on('click', 'ul li a', function() {
	$(this).addClass('active').parent().siblings().children().removeClass('active');	
})

