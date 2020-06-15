var optionsWithSndChoice = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor];

function getData(type) {
	if (optionsWithSndChoice.includes(type)) {
		switch (type) {
		case vehicleRouteLength:
			getVehicleList();
			break;
		case vehicleSpeed:
			getVehicleList();
			break;
		case vehicleSpeedFactor:
			getVehicleList();
			break;
		}
		return; //we need more input before we can show the graph
	} else {
		switch (type) {
		case avgRouteLength:
			getAvgRouteLength();
			break;
		case avgSpeed:
			getAvgSpeed();
			break;
		case avgSpeedFactor:
			getAvgSpeedFactor();
			break;
		case arrivedVehicles:
			getArrivedVehicles();
			break;
		case transferredVehicles:
			getTransferredVehicles();
			break;
		case runningVehicles:
			getRunningVehicles();
			break;
		default: 
			;
			break;
		}
	}

}



function getDataSnd(type) {
	getVehicleSpeed(type);
}

function fileClick(id) {
	//TODO update graph;
}

function handleDataResponse(response, label) {
		myObj = JSON.parse(response);
		var result = "[";
			for (var i = 0; i < myObj.length; i++) {
				result += "{ \"x\": " + myObj[i].XValue + ", \"y\": " + myObj[i].YValue + " }";
				if (i < myObj.length -1) {
					result += ", ";
				}
			}
		result += "]";
	changeData(result, label); //TODO This might not be the best place to call this method
	}

function openXhrGETRequest(xhr, url) {
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
}

function getVehicleSpeed(vehicle_id) {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/vehiclespeed?vehicle=" + vehicle_id;
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Vehicle speed of " + vehicle_id);
		}
	}
	xhr.send();
}

function getVehicleSpeedFactor(vehicle_id) {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/vehiclespeedfactor?vehicle=" + vehicle_id;
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Vehicle speed factor of " + vehicle_id);
		}
	}
	xhr.send();
}

function getAvgRouteLength() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgroutelength";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			handleDataResponse(response, "Average route length");
		}
	}
	xhr.send();
}

function getAvgSpeed() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgspeed";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Average speed");
		}
	}
	xhr.send();
}

function getAvgSpeedFactor() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgspeedfactor";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Average speed factor");
		}
	}
	xhr.send();
}

function getArrivedVehicles() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/arrivedvehicles";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Cumulative number of arrived vehicles");
		}
	}
	xhr.send();
}

function getTransferredVehicles() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/transferredvehicles";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Number of transferred vehicles");
		}
	}
	xhr.send();
}

function getRunningVehicles() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/runningvehicles";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		handleDataResponse(response, "Number of running vehicles");
		}
	}
	xhr.send();
}

function getVehicleList() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/vehiclelist";
	openXhrGETRequest(xhr, url);
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			myObj = JSON.parse(response);
			secDropDownOptions['Route length'] = [];
			secDropDownOptions['Speed'] = [];
			secDropDownOptions['Speed factor'] = [];
				secDropDownOptions['Route length'] = myObj;
				secDropDownOptions['Speed'] = myObj;
				secDropDownOptions['Speed factor'] = myObj;
		}
	}
	xhr.send();
}