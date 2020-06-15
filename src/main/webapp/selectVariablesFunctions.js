function editChartStyle(type){
	var checked = document.querySelector('input[name="editChart"]:checked').value;
	switch(checked) {
	case 'title':
		if (type === "remove") {
			chart.options.title.display = false;
		} else if (type === "change"){
		var newTitle = document.getElementById("setTitleInput").value;
		chart.options.title.text = newTitle;
		chart.options.title.display = true;
		}
		break;
	case 'y-axis':
		if (type === "remove") {
			chart.options.scales.yAxes[0].scaleLabel.display = false;
		} else if (type === "change"){
		var newTitle = document.getElementById("setTitleInput").value;
		chart.options.scales.yAxes[0].scaleLabel.labelString = newTitle;
		chart.options.scales.yAxes[0].scaleLabel.display = true;
		}
		break;
	case 'x-axis':
		if (type === "remove") {
			chart.options.scales.xAxes[0].scaleLabel.display = false;
		} else if (type === "change"){
		var newTitle = document.getElementById("setTitleInput").value;
		chart.options.scales.xAxes[0].scaleLabel.labelString = newTitle;
		chart.options.scales.xAxes[0].scaleLabel.display = true;
		}
		break;
	case 'color':
		if (type === "change"){
		var newColor = document.getElementById("setTitleInput").value;
		chart.data.datasets[0].borderColor = newColor;
		chart.data.datasets[0].backgroundColor = newColor;
		}
		break;
	}
	chart.update();
}

var fstDropDownOptions = {};
var secDropDownOptions = {};

function loadFstDropDownOptions(){
	fstDropDownOptions['line'] = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor, avgRouteLength, avgSpeed, avgSpeedFactor, arrivedVehicles, transferredVehicles, runningVehicles];
	fstDropDownOptions['scatter'] = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor, avgRouteLength, avgSpeed, avgSpeedFactor, arrivedVehicles, transferredVehicles, runningVehicles];
	fstDropDownOptions['pie'] = [vehicleRouteLength, edgeFrequency];
}

function loadSecDropDownOptions() {
	secDropDownOptions[edgeFrequency] = ['Edge 1', 'Edge 2', 'Edge 3'];
	secDropDownOptions[laneTransitingVehicles] = ['Lane 1', 'Lane 2', 'Lane 3'];
	secDropDownOptions[vehicleRouteLength] = [];
	secDropDownOptions[vehicleSpeed] = [];
	secDropDownOptions[vehicleSpeedFactor] = [];
	getVehicleList();
}

function changeFirstChoice(chartType) {
	var menu = document.getElementById("first-choice");
	var newOptions = fstDropDownOptions[chartType];
	editDropDown(menu, newOptions);
	if(chartType != 'textElement') {   
		//Hide editor, show variable selector
		document.getElementById("chart").style.visibility="visible";  
		document.getElementById("textEditor").style.visibility="hidden";  
		
		var menu = document.getElementById("first-choice");
		var newOptions = fstDropDownOptions[chartType];
		editDropDown(menu, newOptions);
	} else {
		//Show editor, hide variable selector
		document.getElementById("chart").style.visibility="hidden";  
		document.getElementById("textEditor").style.visibility="visible";
		
	}
}

function changeSecondChoice() {
    var fst = document.getElementById("first-choice");
    var sec = document.getElementById("second-choice");
    if (sec == null) {
    	document.getElementById("optionalSecChoice").innerHTML = "<select id=\"second-choice\" name=\"detailChoice\" class=\"form-control\" onchange=\"getDataSnd(this.value)\"></select>";
    	sec = document.getElementById("second-choice");
    	}
    var chosen = fst.options[fst.selectedIndex].value;
    getData(chosen); //TODO obviously this is not really the right place for this, oops
    var newOptions = secDropDownOptions[chosen];
    editDropDown(sec, newOptions);
}

function editDropDown(menu, newOptions) {
	while (menu.options.length) {
        menu.remove(0);
    }
    if (newOptions) {
        var i;
        for (i = 0; i < newOptions.length; i++) {
            var option = new Option(newOptions[i], newOptions[i]);
            menu.options.add(option);
        }
    } else {
    	document.getElementById("optionalSecChoice").innerHTML = "";
    }
    //console.log("drop down was edited");
}