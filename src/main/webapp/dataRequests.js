var optionsWithSndChoice = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor];
var urlInit = "/sumo-dashboard/rest/simulations/id/";

//wait for more input if necessary, if not, get data with the proper path
function dataSwitch(type) {
	if (optionsWithSndChoice.includes(type)) {
		return; //we need more input before we can show the graph
	} else {
		switch (type) {
		case avgRouteLength:
			getData(type, "avgroutelength");
			break;
		case avgSpeed:
			getData(type, "avgspeed");
			break;
		case avgSpeedFactor:
			getData(type, "avgspeedfactor");
			break;
		case arrivedVehicles:
			getData(type, "arrivedvehicles");
			break;
		case transferredVehicles:
			getData(type, "transferredvehicles");
			break;
		case runningVehicles:
			getData(type, "runningvehicles");
			break;
		case edgeFrequencyInitial:
			getData(type, "edgefrequencyinitial");
			break;
		case routeLengthInitial:
			getData(type, "routelengthinitial");
			break;
		}
	}
}

//function called when a parameter is selected in second drop down menu. Gets data with proper path and parameter
function dataSndSwitch(paramID) {
	var element = document.getElementById("first-choice")
	dataType = element.options[element.selectedIndex].value;
	switch (dataType) {
	case edgeFrequency:
		getDataWithParam(dataType, "edgefrequency", "edge", paramID);
		break;
	case laneTransitingVehicles:
		getDataWithParam(dataType, "lanetransitingvehicles", "lane", paramID);
		break;
	case vehicleRouteLength:
		getDataWithParam(dataType, "vehicleroutelength", "vehicle", paramID);
		break;
	case vehicleSpeed:
		getDataWithParam(dataType, "vehiclespeed", "vehicle", paramID);
		break;
	case vehicleSpeedFactor:
		getDataWithParam(dataType, "vehiclespeedfactor", "vehicle", paramID);
		break;
	}
}

//response to a sumo file being selected, load all options lists and reset chart if necessary
function fileClick(id) {
	if (!addDataSetBoolean) {
		resetChart();
	}
	getOptionList("lane", "lanelist");
	getOptionList("vehicle", "vehiclelist");
	getOptionList("edge", "edgelist");
	//if 'add dataset' is not clicked, reset chart
}

//parse the response of the request to a sorted list of datapoints for the graph
function handleGraphDataResponse(JSONResponse, label) {
	var response = JSON.parse(JSONResponse);
	const length = Object.keys(response).length;
	const sorted = [];
	for (var key in response) {
		sorted.push({key: key, value: response[key]});
	}
	sorted.sort((a,b) => a.key - b.key);
	
	var result = "[";
	for (var i = 0; i < sorted.length; i++) {
		result += "{ \"x\": " + sorted[i].key + ", \"y\": " + sorted[i].value + " },";
	}
	if (result.length > 1) result = result.substring(0, result.length -1);
	result += "]";
	changeGraphData(result, label);
}

//parse the response of the request to a list of labels and a corresponding list of data for the pie/bar chart
function handleChartDataResponse(JSONResponse, dataType) {
	var response = JSON.parse(JSONResponse);
	var data = "[";
	var labels = "[";
	var i = 0;
	for (var key in response) {
		labels += "\"" + key + "\", ";
		data += response[key] + ", ";
	}
	labels = labels.substring(0, labels.length -2);
	data = data.substring(0, data.length -2);
	labels += "]";
	data += "]";
	changeChartData(data, labels, dataType);	
}

//open the xmlHttpRequest
function openXhrGETRequest(xhr, url, wait) {
	xhr.open("GET", url, wait);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
}

//Send a datarequest with a parameter
function getDataWithParam(dataType, path, paramName, paramID) {
	var simID = getSelectedID();
	if (simID === null) {
		alert("Please select a simulation first.");
		return;
	}

	var xhr = new XMLHttpRequest();
	var url = urlInit + simID + "/" + path + "?" + paramName + "=" + paramID;
	openXhrGETRequest(xhr, url, true);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleGraphDataResponse(this.responseText, dataType + " of " + paramID);
		}
		else if (this.readyState == 4 && this.status == 401) {
			location.href ="loginPage.html";
		}
		else if (this.readyState == 4) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Load tags response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

//send a datarequest without a parameter
function getData(dataType, path) {
	var simid = getSelectedID();
	if (simid === null) {
		alert("Please select a simulation first.");
		return;
	}
	
	var xhr = new XMLHttpRequest();
	var pathName = path;
	var url = urlInit + simid + "/" + pathName;
	openXhrGETRequest(xhr, url, true);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (fstDropDownOptions['pie'].includes(dataType)) {
				handleChartDataResponse(this.responseText, dataType);
			} else {
				handleGraphDataResponse(this.responseText, dataType);
			}
		}
		else if (this.readyState == 4 && this.status == 401) {
			location.href ="loginPage.html";
		}
		else if (this.readyState == 4) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Get data response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

//send a request to get the lanelist, vehiclelist or edgelist
function getOptionList(listType, path) {
	var simid = getSelectedID();
	if (simid == null) {
		alert("Please select a simulation first.");
		return;
	}
	
	var xhr = new XMLHttpRequest();
	var url = urlInit + simid + "/" + path;
	openXhrGETRequest(xhr, url, false);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleOptionListResponse(this.responseText, listType);
		}
		else if (this.readyState == 4 && this.status == 401) {
			location.href ="loginPage.html";
		}
		else if (this.readyState == 4) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Load tags response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

//handle the response of a request for a list by saving the list to the drop down menu variables
function handleOptionListResponse(JSONResponse, listType) {
	var response = JSON.parse(JSONResponse);
	switch (listType) {
	case "vehicle":
		secDropDownOptions[vehicleRouteLength] = response;
		secDropDownOptions[vehicleSpeed] = response;
		secDropDownOptions[vehicleSpeedFactor] = response;
		break;
	case "lane":
		secDropDownOptions[laneTransitingVehicles] = response;
		break;
	case "edge":
		secDropDownOptions[edgeFrequency] = response;
		break;
	}
}

//request the summary statistics
function getSummaryStatistics() {
	var simid = getSelectedID();
	if (simid == null) {
		alert("Please select a simulation first.");
		return;
	}
	
	var xhr = new XMLHttpRequest();
	var pathName = "summarystatistics";
	var url = urlInit + simid + "/" + pathName;
	openXhrGETRequest(xhr, url, true);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleSummaryStatisticsResponse(this.responseText);
		}
		if (this.readyState == 4 && this.status != 200) {
			alert("Error occured when getting summary statistics, status: " + this.status);
			console.error("Get summary statistics response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

function handleSummaryStatisticsResponse(JSONresponse) {
	var response = JSON.parse(JSONresponse);
	var result = "<p id=\"sumStats\"><strong>Summary Statistics</strong><br>";
	result += "Number of edges: " + response.edges + "<br>";
	result += "Number of junctions: " + response.junctions + "<br>";
	result += "Number of vehicles: " + response.vehicles + "<br>";
	result += "<p>";
	viewSummaryStatistics(result);
}

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