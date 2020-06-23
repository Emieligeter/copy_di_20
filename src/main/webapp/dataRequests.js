var optionsWithSndChoice = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor];
var urlInit = "http://localhost:8080/sumo-dashboard/rest/simulations/id/";
//var urlInit = "http://env-di-team1.paas.hosted-by-previder.com/sumo-dashboard/rest/simulations/id/";
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


function dataSndSwitch(paramID) {
	var element = document.getElementById("first-choice")
	dataType = element.options[element.selectedIndex].value;
	console.log(dataType + ", " + paramID + " @dataRequest");
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

function fileClick(id) {
	getOptionList("lane", "lanelist");
	getOptionList("vehicle", "vehiclelist");
	getOptionList("edge", "edgelist");
	//TODO update graph;
}

function handleDataResponse(JSONResponse, label) {
	var response = JSON.parse(JSONResponse);
	console.log(response);
	const length = Object.keys(response).length;
	
	const sorted = [];
	for (var key in response) {
		sorted.push({key: key, value: response[key]});
	}
	sorted.sort((a,b) => a.key - b.key);
	console.log(sorted);
	
	var result = "[";
	for (var i = 0; i < sorted.length; i++) {
		result += "{ \"x\": " + sorted[i].key + ", \"y\": " + sorted[i].value + " },";
		console.log(sorted[i]);
	}
	result = result.substring(0, result.length -1);
	result += "]";
	console.log(result);
	changeGraphData(result, label);
	}

function handleChartDataResponse(JSONResponse, dataType) {
	var response = JSON.parse(JSONResponse);
	var data = "[";
	var labels = "[";
	var i = 0;
	for (var key in response) {
		/*if (i < 5) {*/
		labels += "\"" + key + "\", ";
		data += response[key] + ", ";
		//}
		//i++;
	}
	labels = labels.substring(0, labels.length -2);
	data = data.substring(0, data.length -2);
	labels += "]";
	data += "]";
	changeChartData(data, labels, dataType);	
}

function openXhrGETRequest(xhr, url, wait) {
	xhr.open("GET", url, wait);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.setRequestHeader('Authorization', 'Bearer 12345');
}

function getDataWithParam(dataType, path, paramName, paramID) {
	var simID = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = urlInit + simID + "/" + path + "?" + paramName + "=" + paramID;
	openXhrGETRequest(xhr, url, true);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleDataResponse(this.responseText, dataType + " of " + paramID);
		}
		if (this.readyState == 4 && this.status != 200) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Load tags response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

function getData(dataType, path) {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var pathName = path;
	console.log(pathName);
	var url = urlInit + simid + "/" + pathName;
	openXhrGETRequest(xhr, url, true);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (fstDropDownOptions['pie'].includes(dataType)) {
				handleChartDataResponse(this.responseText, dataType);
			} else {
				handleDataResponse(this.responseText, dataType);
			}
		}
		if (this.readyState == 4 && this.status != 200) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Load tags response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

function getOptionList(listType, path) {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = urlInit + simid + "/" + path;
	openXhrGETRequest(xhr, url, false);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleOptionListResponse(this.responseText, listType);
		}
		if (this.readyState == 4 && this.status != 200) {
			alert("Error occured when getting data, status: " + this.status);
			console.error("Load tags response:\n" + JSON.stringify(this.responseText));
		}
	}
	xhr.send();
}

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