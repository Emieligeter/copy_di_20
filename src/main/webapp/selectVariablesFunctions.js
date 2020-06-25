//response to the "edit chart" menu. function is called with either 'change' or 'remove' (type)
//depending on the radio button selected at that time, the value entered in the text field is put in the correct place
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
		chart.data.datasets[dataSetNumber].borderColor = newColor;
		chart.data.datasets[dataSetNumber].backgroundColor = newColor;
		}
		break;
	case 'legend':
		if (type=== "change") {
			var newLabel = document.getElementById("setTitleInput").value;
			label[dataSetNumber] = newLabel;
			chart.data.datasets[dataSetNumber].label = label[dataSetNumber];
		}
		break;
	}
	chart.update();
}

//list with options for the drop down menus
var fstDropDownOptions = {};
var secDropDownOptions = {};
fstDropDownOptions['line'] = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor, avgRouteLength, avgSpeed, avgSpeedFactor, arrivedVehicles, transferredVehicles, runningVehicles];
fstDropDownOptions['scatter'] = [edgeFrequency, laneTransitingVehicles, vehicleRouteLength, vehicleSpeed, vehicleSpeedFactor, avgRouteLength, avgSpeed, avgSpeedFactor, arrivedVehicles, transferredVehicles, runningVehicles];
fstDropDownOptions['pie'] = [edgeFrequencyInitial];
fstDropDownOptions['bar'] = [edgeFrequencyInitial];

//change the first drop down menu to a new set of options, corresponding to the graphtype
function changeFirstChoice() {
	if (chartType === 'textElement') {   
		//Show editor, hide variable selector, hide summary statistics
		document.getElementById("chart").style.visibility="hidden";  
		document.getElementById("textEditor").style.visibility="visible";
		document.getElementById("summaryStatistics").style.visibility="hidden";  
	} else if (chartType === 'sumStats') {
		//Hide editor, hide variable selector, show summary statistics
		document.getElementById("chart").style.visibility="hidden";  
		document.getElementById("textEditor").style.visibility="hidden";
		document.getElementById("summaryStatistics").style.visibility="visible";
		getSummaryStatistics();
	} else {
		//Hide editor and summary statistics, show variable selector
		document.getElementById("chart").style.visibility="visible";  
		document.getElementById("textEditor").style.visibility="hidden";
		document.getElementById("summaryStatistics").style.visibility="hidden";  
		//edit drop down menu
		var menu = document.getElementById("first-choice");
		var newOptions = fstDropDownOptions[chartType];
		editDropDown(menu, newOptions);
	}
}

//change the second drop down menu to a new set of options, corresponding to the earlier chosen plot 
function changeSecondChoice() {
    var fst = document.getElementById("first-choice");
    var sec = document.getElementById("second-choice");
    if (sec == null) {
    	document.getElementById("optionalSecChoice").innerHTML = "<select id=\"second-choice\" name=\"detailChoice\" class=\"form-control\" onchange=\"dataSndSwitch(this.value)\"></select>";
    	sec = document.getElementById("second-choice");
    	}
    var chosen = fst.options[fst.selectedIndex].value;
    dataSwitch(chosen); //call the function that request the data for the value chosen in the first drop down menu
    var newOptions = secDropDownOptions[chosen];
    editDropDown(sec, newOptions);
}

//change the given drop down menu to the options given (newOptions), by removing all existing options and creating the new ones
function editDropDown(menu, newOptions) {
	while (menu.options.length) {
        menu.remove(0);
    }
    if (newOptions) {
        var option = new Option("Please choose a variable", "", true, false);
        menu.options.add(option);
    	var i;
        for (i = 0; i < newOptions.length; i++) {
            var option = new Option(newOptions[i], newOptions[i]);
            menu.options.add(option);
        }
    } else {
    	//if no new options exist, hide the second drop down menu
    	document.getElementById("optionalSecChoice").innerHTML = "";
    }
}