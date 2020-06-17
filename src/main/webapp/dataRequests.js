var optionsWithSndChoice = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor];
var urlInit = "http://localhost:8080/sumo-dashboard/rest/simulations/id/";
function dataSwitch(type) {
	if (optionsWithSndChoice.includes(type)) {
		return; //we need more input before we can show the graph
	} else {
		switch (type) {
		case avgRouteLength:
			//getAvgRouteLength();
			getData(type, "avgroutelength");
			break;
		case avgSpeed:
			//getAvgSpeed();
			getData(type, "avgspeed");
			break;
		case avgSpeedFactor:
			//getAvgSpeedFactor();
			getData(type, "avgspeedfactor");
			break;
		case arrivedVehicles:
			//getArrivedVehicles();
			getData(type, "arrivedvehicles");
			break;
		case transferredVehicles:
			//getTransferredVehicles();
			getData(type, "transferredvehicles");
			break;
		case runningVehicles:
			//getRunningVehicles();
			var path = "runningvehicles";
			console.log(path);
			getData(type, path);
			break;
		default: 
			;
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
	console.log("lists are loaded");
	//TODO update graph;
}

function handleDataResponse(JSONResponse, label) {
	var response = JSON.parse(JSONResponse);
	var result = "[";
	for (var key in response) {
		result += "{ \"x\": " + key + ", \"y\": " + response[key] + " },";
	}
	result = result.substring(0, result.length -1);
	result += "]";
	console.log(result);
	changeData(result, label); //TODO This might not be the best place to call this method
	}

function openXhrGETRequest(xhr, url, wait) {
	xhr.open("GET", url, wait);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
}

function getDataWithParam(dataType, path, paramName, paramID) {
	var simID = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = urlInit + simID + "/" + path + "?" + paramName + "=" + paramID;
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		handleDataResponse(this.responseText, dataType + " of " + paramID);
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
			handleDataResponse(this.responseText, dataType);
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
	}
	xhr.send();
}

function handleOptionListResponse(JSONResponse, listType) {
	var response = JSON.parse(JSONResponse);
	console.log("handling list for " + listType);
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