/**
 * This file contains all javascript functions regarding the list of SUMO files.
 */
function loadFiles() {
	var httpReq = new XMLHttpRequest();
	var sumoFiles = document.getElementById("sumoFiles");
	httpReq.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
		  var response = this.responseText;
		  var res = JSON.parse(response);
		  console.log(res);
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
			  "<small id=\"fileTags\">" + tags + "</small>\n" +
			  "</a>";
			  sumoFiles.appendChild(liElem);
		  }
	  }
	}
	httpReq.open("GET", "/sumo-dashboard/rest/simulations", true);
	httpReq.send();
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
	//Makes clicked elem active and all other list elems inactive
	$(this).addClass('active').parent().siblings().children().removeClass('active');
	//Display current metadata of clicked sumo file
	showMetaData($(this).parent().attr('id'));
})

function showMetaData(id) {
	var url = "/sumo-dashboard/rest/simulations/id/" + id;
	$.get(url, function(data, status){
		document.getElementById("newTitle").setAttribute("value", data.name);
		document.getElementById("newDate").setAttribute("value", data.date);
		var researcher = (data.researcher === undefined) ? "undefined" : data.researcher;
		document.getElementById("newResearcher").setAttribute("value", researcher);
		document.getElementById("newDescription").innerHTML = data.description;
	});
}

function getSelectedID() {
	return $("a").filter(".active").parent().attr("id");
}

$("#deleteSimButton").click(function() {
	event.preventDefault(); // prevent default action
	
	var url = "/sumo-dashboard/rest/simulations/id/" + getSelectedID();
	console.log(url);
	$.ajax({
		url: url,
		type: "DELETE"
	});
})

