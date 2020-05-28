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

var dropDownOptions = {};

function loadDropDownOptions(){
	dropDownOptions['Edge appearance frequency'] = ['Edge 1', 'Edge 2', 'Edge 3'];
	dropDownOptions['Number of lane transiting vehicles'] = ['Lane 1', 'Lane 2', 'Lane 3'];
	dropDownOptions['Route length'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
	dropDownOptions['Speed'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
	dropDownOptions['Speed factor'] = ['Vehicle 1', 'Vehicle 2', 'Vehicle 3'];
	console.log("loadDropDownOptions successfull")
}

function changeSecondChoice() {
    var fst = document.getElementById("first-choice");
    var sec = document.getElementById("second-choice");
    if (sec == null) {
    	document.getElementById("optionalSecChoice").innerHTML = "<select id=\"second-choice\" name=\"detailChoice\" class=\"form-control\"></select>";
    	sec = document.getElementById("second-choice");
    	}
    var chosen = fst.options[fst.selectedIndex].value;
    while (sec.options.length) {
        sec.remove(0);
    }
    var options = dropDownOptions[chosen];
    if (options) {
        var i;
        for (i = 0; i < options.length; i++) {
            var option = new Option(options[i], i);
            sec.options.add(option);
        }
    } else {
    	document.getElementById("optionalSecChoice").innerHTML = "";
    }
} 