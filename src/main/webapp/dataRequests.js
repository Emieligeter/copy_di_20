function getAvgSpeedTime() {
	console.log("getAvgSpeedTime was reached");
	
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "http://localhost:8080/sumo-dashboard/rest/simulations/id/13985/avgspeedtime");
		xhr.setRequestHeader('Access-Control-Allow-Headers', '*');
	    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	    console.log('ok for getavgspeedtime');
		xhr.send();
		console.log("request was sent");
		xhr.onreadystatechange = function() {
			console.log("ready state changed to " + this.readyState + " status " + this.status);
			if (this.readyState == 4 && this.status == 200) {
				console.log("response is received");
				var response = this.responseText;
				console.log("dit is de response " + response);
				myObj = JSON.parse(response);
				var result = "[";
					for (var i = 0; i < myObj.length; i++) {
						result += "{ \"x\": " + myObj[i].XValue + ", \"y\": " + myObj[i].YValue + " }";
						if (i < myObj.length -1) {
							result += ", ";
						}
					}
				result += "]";
			console.log("CHECK THIS OUT: " + result);
			changeData(result); //TODO This might not be the best place to call this method
			}
		}
		console.log("end of getavgspeedtime");
}