//the general chart and it's variables
var chart;
var chartType = "line";
var data = [];
var label = [];
var options = {};
options["line"] = {scales: {xAxes: [{type: 'linear', display: true, scaleLabel: {display: true}}]}};
options["scatter"] = {}; 
options["bar"] = {};
options["pie"] = {};

//the number of the newest dataset
var dataSetNumber = 0;
//boolean, true if the "add dataset" button was selected
var addDataSetBoolean = false;

//function that is called for the "add dataset" button
function addDataSet() {
	addDataSetBoolean = true;
	document.getElementById("selectNewMsg").style.display = "block";
}

//function that is called for the "reset" button
function resetChart() {
	if (this.chart !== undefined && this.chartType !== 'sumStats' && this.chartType !== 'textElement') {
	data = [];
	label = [];
	chart.data.datasets=[{label: label[0], data: data[0], fill: false}];
	chart.data.labels = [];
	dataSetNumber = 0;
	//reset all data, but leave title and axis labels
	chart.update();
	}
}

//fill in the numbers gotten by the datarequest 'getSummaryStatistics'
function viewSummaryStatistics(sumStatsString) {
	document.getElementById('summaryStatistics').innerHTML = sumStatsString;
}

//function called for any of the chart type buttons
function setChartType(type) {
	resetChart();
	chartType = type;
	//only show addDataSetButton if appropriate
	if (chartType === 'bar' || chartType === 'pie') {
		document.getElementById("addDataSetButton").style.display = "none";
	} else {
		document.getElementById("addDataSetButton").style.display = "block";
	}
	//only show the appropriate selectVariables buttons
	if (chartType === 'pie') {
		document.getElementById("xAxisButton").style.display = "none";
		document.getElementById("yAxisButton").style.display = "none";
		document.getElementById("legendButton").style.display = "none";
	} else {
		document.getElementById("xAxisButton").style.display = "block";
		document.getElementById("yAxisButton").style.display = "block";
		document.getElementById("legendButton").style.display = "block";
	}
	//edit the drop down menu options
	changeFirstChoice(type);
	updateChart();
}

//destroy the old chart and create a new one, with type, label, data and options given above
function updateChart() {
	if (this.chart !== undefined) {
		this.chart.destroy();
	}
	var canvas = document.getElementById('reportElement');
	var ctx = canvas.getContext('2d');
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	if(chartType !== "textElement" && chartType !== "sumStats") {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	chart = new Chart(ctx, {
		type : chartType,
		data : {
			datasets: [{
				label: label[0],
				data: data[0],
				fill: false,
			}]
			},
		options : options[chartType]
	});
	}
}

//update the data for a linegraph or scatter plot. Add them as new dataset if that button was clicked
function changeGraphData(data, label) {
	if (addDataSetBoolean) {
		dataSetNumber++;
		addDataSetBoolean = false;
		document.getElementById("selectNewMsg").style.display = "none";
		this.data[dataSetNumber] = JSON.parse(data);
		this.label[dataSetNumber] = label;
		chart.data.datasets.push({data: this.data[dataSetNumber], label: this.label[dataSetNumber], fill: false});
	} else {
		this.data[dataSetNumber] = JSON.parse(data);
		this.label[dataSetNumber] = label;
		chart.data.datasets[dataSetNumber].data = this.data[dataSetNumber];
		chart.data.datasets[dataSetNumber].label = this.label[dataSetNumber];
	}
	chart.data.labels = [];
	chart.update();
}

//update the data for a bar or pie chart.
function changeChartData(data, labels, label) {
	this.data[0] = JSON.parse(data);
	chart.data.datasets[0].data = this.data[0];
	this.label[0] = label;
	chart.data.datasets[0].label = this.label[0];
	chart.data.labels = JSON.parse(labels);
	chart.data.datasets[0].backgroundColor = chartColours(this.data[0].length);
	chart.update();
}

function chartColours(size) {
    var colourSet = ['#6C8EAD', '#F25F5C', '#F17F29', '#FFE066', '#4E937A', '#70CAD1', '#CBBAED', '#1D3354', '#34313B', '#919191'];
    var coloursToBeUsed = [];
    for(var i = 0; i < size; i++) {
    	coloursToBeUsed.push(colourSet[i%colourSet.length]);
    }
    return coloursToBeUsed;
  }
