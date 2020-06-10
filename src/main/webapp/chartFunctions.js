/**
 * 
 */

var chart;
var chartType = "line";
var data;


function setChartType(type) {
	chartType = type;
	changeFirstChoice(type);
	//console.log("Set chartType to " + type + chartType);
	updateChart();
	getAvgSpeedTime(); //TODO obviously in the final result this needs to go somewhere else
}

function setData(newdata) {
	data = JSON.parse(newdata);
	//console.log("data is set to: " + data);
	updateChart();
}

function updateChart() {
	//console.log("We're going to update the chart to type " + chartType);
	if (this.chart !== undefined) {
		this.chart.destroy();
	}
	var canvas = document.getElementById('myChart');
	var ctx = canvas.getContext('2d');
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	chart = new Chart(ctx, {
		// The type of chart we want to create
		type : chartType,

		// The data for our dataset
		data : {
			datasets: [{
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
	//console.log("done.");
}

function changeData(data) {
	//chart.data.datasets[0].data.pop();
	this.data = JSON.parse(data);
	chart.data.datasets[0].data = this.data;
	chart.update();
}
