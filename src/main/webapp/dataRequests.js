var optionsWithSndChoice = ['Edge appearance frequency', 'Number of lane transiting vehicles', 'Route length', 'Speed', 'Speed factor'];

function getData(type) {
	console.log("getData was called with type: " + type);
	if (optionsWithSndChoice.includes(type)) {
		return; //we need more input before we can show the graph
	} else {
		switch (type) {
		case "Average vehicle speed":
			getAvgSpeedTime();
			break;
		case "Average route length":
			getAvgRouteLengthTime();
			break;
		default: 
			;
			break;
		}
	}

}

function fileClick(id) {
//TODO update graph;
}

function getDataSnd(type) {
	console.log("getDataSnd was called with type: " + type);
}

function responseReceived(label) {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
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
}

function getAvgSpeedTime() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgspeedtime";
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = responseReceived("Average speed");
	xhr.send();
}

function getAvgRouteLength() {
	var simid = getSelectedID();
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/sumo-dashboard/rest/simulations/id/" + simid + "/avgroutelength";
	xhr.open("GET", url);
	xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	xhr.onreadystatechange = responseReceived("Average route length");
	xhr.send();
}