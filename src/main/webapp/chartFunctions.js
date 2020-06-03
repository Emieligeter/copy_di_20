/**
 * 
 */
//src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"

var chart;
var chartType = "line";
var data = [{
    x: 10,
    y: 20
}, {
    x: 15,
    y: 10
}, {x: 0, y: 30}, {x:30, y: 5}

];



function getCtx() {
	document.getElementById('myChart').getContext('2d');
	console.log("successfull");
};

function setChartType(type) {
	chartType = type;
	changeFirstChoice(type);
	console.log("Set chartType to " + type + chartType);
	updateChart();
}

function updateChart() {
	console.log("We're going to update the chart to type " + chartType);
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
				    label: 'Average speed',
				    fill: false,
				    data: getAvgSpeedTime(),
				  }]
				},

		// Configuration options go here
		options : {//	elements: { line: { tension: 0 }}}
			
		}
	});
	console.log("done.");
}

/*https://stackoverflow.com/questions/28828915/how-set-color-family-to-pie-chart-in-chart-js*/
function getColors(length) {
	let pallet = [ "#0074D9", "#FF4136", "#2ECC40", "#FF851B", "#7FDBFF",
			"#B10DC9", "#FFDC00", "#001f3f", "#39CCCC", "#01FF70", "#85144b",
			"#F012BE", "#3D9970", "#111111", "#AAAAAA" ];
	let colors = [];

	for (let i = 0; i < length; i++) {
		colors.push(pallet[i % pallet.length]);
	}

	return colors;
}
