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
	fstDropDownOptions['line'] = ['Edge appearance frequency', 'Number of lane transiting vehicles', 'Route length', 'Speed', 'Speed factor', 'Average route length', 'Average vehicle speed', 'Average vehicle speed factor', 'Cumulative number of arrived vehicles', 'Number of transferred vehicles', 'Number of running vehicles'];
	fstDropDownOptions['scatter'] = ['Edge appearance frequency', 'Number of lane transiting vehicles', 'Route length', 'Speed', 'Speed factor', 'Average route length', 'Average vehicle speed', 'Average vehicle speed factor', 'Cumulative number of arrived vehicles', 'Number of transferred vehicles', 'Number of running vehicles'];
	fstDropDownOptions['pie'] = ['Route length', 'Edge appearance frequency'];
}

function loadSecDropDownOptions(){
	secDropDownOptions['Edge appearance frequency'] = ['Edge 1', 'Edge 2', 'Edge 3'];
	secDropDownOptions['Number of lane transiting vehicles'] = ['Lane 1', 'Lane 2', 'Lane 3'];
	secDropDownOptions['Route length'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
	secDropDownOptions['Speed'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
	secDropDownOptions['Speed factor'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
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