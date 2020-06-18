/**
 * 
 */

var chart;
var chartType = "line";
var data;
var label;


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
	if(chartType == "textElement"){
	} else {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	chart = new Chart(ctx, {
		// The type of chart we want to create
		type : chartType,

		// The data for our dataset
		data : {
			datasets: [{
				label: label,
				data: data,
				fill: false
			}]
			},
		// Configuration options go here
		options : {	
			scales: {
				xAxes: [{
					type: 'linear',
					display: true,
					scaleLabel: {
						display: true
					}
				}]
			}
		}
	});
	}
}

function changeGraphData(data, label) {
	this.data = JSON.parse(data);
	chart.data.datasets[0].data = this.data;
	this.label = label;
	chart.data.datasets[0].label = this.label;
	chart.data.labels = [];
	chart.update();
}

function changeChartData(data, labels, label) {
	this.data = JSON.parse(data);
	chart.data.datasets[0].data = this.data;
	this.label = label;
	chart.data.datasets[0].label = this.label;
	chart.data.labels = JSON.parse(labels);
	chart.update();
}
