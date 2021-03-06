/**
 * This file contains all javascript functions regarding the list of SUMO files.
 */

//Loads all SUMO files with their metadata into a list
function loadFiles() {	
	var httpReq = new XMLHttpRequest();
	var sumoFiles = document.getElementById("sumoFiles");
	httpReq.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
		  var response = this.responseText;
		  var res = JSON.parse(response);
		  //For every file, create a list element and fill it with its metadata
		  for (var i = 0; i < res.length; i++) {
			  var liElem = document.createElement("li");
			  liElem.id = res[i].ID;
			  var name = res[i].name;
			  var date = res[i].date;
			  var researcher = res[i].researcher;
			  var tags = res[i].tags;
			  liElem.innerHTML = "<a href=\"#\" class=\"list-group-item list-group-item-action flex-column align-items-start\">\n" +
			  "<div class=\"d-flex w-100 justify-content-between\">\n" +
			  "<h5 id=\"fileName\" class=\"mb-1\">" + name + "</h5>\n" +
			  "</div>\n" +
			  "<p id=\"fileDate\" class=\"mb-1\">Date: " + date + "</p>\n" +
			  "<p id=\"fileResearcher\" class=\"mb-1\">Researcher: " + researcher + "</p>\n" +
			  "<small id=\"fileTags\">Tags: " + tags + "</small>\n" +
			  "</a>";
			  //Add element to list
			  sumoFiles.appendChild(liElem);
		  }
		  //$("ul li[id='" + getSelectedID() + "']").children().click();
	  }
		else if (this.readyState == 4 && this.status == 401) {
			location.href ="loginPage.html";
		}
		else if (this.readyState == 4) {
		  var response = this.responseText;
		  console.error("Load files response:\n" + response);
		  alert("Load files request failed with status: " + this.status);
	  }
	}
	//Request all sumo files
	httpReq.open("GET", "/sumo-dashboard/rest/simulations", true);
	httpReq.send();
}

//Filters the files on current input
function getFilteredFiles() {
  var input, filter, files, li, a, i, txtValue;
  input = document.getElementById('fileSearch');
  filter = input.value.toUpperCase();
  files = document.getElementById("sumoFiles");
  li = files.getElementsByTagName('li');
  // Loop through all list items, hide the ones that don't match the search query
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

//Activates an element in the list
$(document).on('click', 'ul li a', function() {
	//Makes clicked elem active and all other list elems inactive
	$(this).addClass('active').parent().siblings().children().removeClass('active');
	//Display current metadata of clicked SUMO file
	fileClick($(this).parent().attr('id'));
})

//Updates the metadata of a single list element
//Takes its metadata from the current inputs of the form
function updateListElement(id) {
	var newMetadata = document.getElementById("modifyMetadata");
	var activeFile = $('#' + getSelectedID());
	activeFile.children().children().children('#fileName').html(newMetadata.elements[0].value);
	activeFile.children().children('#fileDate').html(newMetadata.elements[1].value);
	activeFile.children().children('#fileResearcher').html("Researcher: " + newMetadata.elements[2].value);
	activeFile.children().children('#fileTags').html("Tags: " + getTagsAsString());
}

//returns the id of the current selected SUMO file
function getSelectedID() {
	return $("a").filter(".active").parent().attr("id");
}
