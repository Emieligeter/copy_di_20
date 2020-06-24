var chart;
var chartType = "line";
var data = [];
var label = [];
var options = {};
var dataSetNumber = 0;
var addDataSetBoolean = false;
options["line"] = {scales: {xAxes: [{type: 'linear', display: true, scaleLabel: {display: true}}]}};
options["scatter"] = {}; 
options["bar"] = {legend: {display: false}};
options["pie"] = {legend: {display: false}};

function addDataSet() {
	addDataSetBoolean = true;
	document.getElementById("selectNewMsg").style.display = "block";
}

function resetChart() {
	data = [];
	label = [];
	chart.data.datasets=[{label: label[0], data: data[0], fill: false}];
	chart.data.labels = [];
	dataSetNumber = 0;
	chart.update();
}

function setChartType(type) {
	chartType = type;
	changeFirstChoice(type);
	updateChart();
}	

function setData(newdata) {
	data = JSON.parse(newdata);
	updateChart();
}


function updateChart() {
	if (this.chart !== undefined) {
		this.chart.destroy();
	}
	var canvas = document.getElementById('reportElement');
	var ctx = canvas.getContext('2d');
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	if(chartType !== "textElement") {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	chart = new Chart(ctx, {
		// The type of chart we want to create
		type : chartType,

		// The data for our dataset
		data : {
			datasets: [{
				label: label[0],
				data: data[0],
				fill: false
			}]
			},
		// Configuration options go here
		options : options[chartType]
	});
	}
}

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

function changeChartData(data, labels, label) {
	this.data[0] = JSON.parse(data);
	chart.data.datasets[0].data = this.data[0];
	this.label[0] = label;
	chart.data.datasets[0].label = this.label[0];
	chart.data.labels = JSON.parse(labels);
	chart.update();
}
