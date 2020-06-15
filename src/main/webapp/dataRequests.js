var optionsWithSndChoice = ['Edge appearance frequency', 'Number of lane transiting vehicles', 'Route length', 'Speed', 'Speed factor'];

function getData(type) {
	console.log("getData was called with type: " + type);
	if (optionsWithSndChoice.includes(type)) {
		switch (type) {
		case "Route length":
			getVehicleList();
			break;
		case "Speed":
			getVehicleList();
			break;
		case "Speed factor":
			getVehicleList();
			break;
		}
		return; //we need more input before we can show the graph
	} else {
		switch (type) {
		case "Average vehicle speed":
			getAvgSpeedTime();
			break;
		case "Average route length":
			getAvgRouteLength();
			break;
		default: 
			;
			break;
		}
	}

}



function getDataSnd(type) {
	console.log("getDataSnd was called with type: " + type);
	getVehicleSpeed(type);
}

function fileClick(id) {
	//TODO update graph;
}

function responseReceived(response, label) {
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

function getAvgSpeedTime() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgspeedtime";
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		responseReceived(response, "Average speed");
		}
	}
	xhr.send();
}

function getAvgRouteLength() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgroutelength";
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			responseReceived(response, "Average route length");
		}
	}
	xhr.send();
}

function getVehicleSpeed(vehicle_id) {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/vehiclespeed?vehicle=" + vehicle_id;
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		responseReceived(response, "Vehicle speed of " + vehicle_id);
		}
	}
	xhr.send();
}


function getVehicleList() {
	console.log("getting vehicle list");
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/vehiclelist";
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			myObj = JSON.parse(response);
			secDropDownOptions['Route length'] = [];
			secDropDownOptions['Speed'] = [];
			secDropDownOptions['Speed factor'] = [];
			//for (var i = 0; i < myObj.length; i++) {
				secDropDownOptions['Route length'] = myObj;
				secDropDownOptions['Speed'] = myObj;
				secDropDownOptions['Speed factor'] = myObj;
		}
	}
	xhr.send();
}