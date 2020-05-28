function chartToReport() {
	console.log("lets add this chart to the report.")
	var chartCopy = copyCanvas(document.getElementById('myChart'));
	document.getElementById('report').appendChild(chartCopy);
	console.log("done.")
}

function copyCanvas(original) {
	var newThing = document.createElement('canvas');
	var cxt = newThing.getContext('2d');
	newThing.width = original.width;
	newThing.height = original.height;
	cxt.scale(0.6, 0.6)
	cxt.drawImage(original, 0, 0);
	return newThing;
}