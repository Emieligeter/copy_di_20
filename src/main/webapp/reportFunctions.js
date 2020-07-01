
//The charttype is checked. If chart type is:
//- textelement, call copyText to get the text content of the editor and place it in the report section
//- summary statistics, call copyTest to get the statistics and place it in the report section
//- anything else, call the copychart function and copy contents of canvas to report section
function chartToReport() {
	var elementCopy;
	if(chartType == 'textElement') {
		var editor = $('#textFrame').contents().find('#editor');
		elementCopy = copyText(editor.cleanHtml());
		editor.clearHtml();
	} else if (chartType === 'sumStats') {
		elementCopy = copyText(document.getElementById('sumStats').innerHTML);
	} else {
		elementCopy = copyCanvas(document.getElementById('reportElement'));
	}
	document.getElementById('report').appendChild(elementCopy);
}

//Receives a text, creates a <p> element and returns it with the given text
function copyText(original) {
	var text =  document.createElement('p');
	text.innerHTML = original;
	return text;
}

//Create an <img> element, convert the contents of the input canvas element to img/png,
//fill the new element and set width and height
function copyCanvas(original) {
	var imgURL = original.toDataURL("image/png");
	var img = document.createElement('img')
	img.src = imgURL;
	img.width = 800;
	img.height = 450;
	img.className = "exportedGraph";
	return img;
}

//Variable used to specify stylesheet to use for report
var printDivCSS = new String ('<link href="styles.css" rel="stylesheet" type="text/css">');

//Requires an ID of the div element that has to be printed
function printDiv(divId) {
    window.frames["print_frame"].document.body.innerHTML=printDivCSS + document.getElementById(divId).innerHTML;
    window.frames["print_frame"].window.focus();
    window.frames["print_frame"].window.print();
}

//Reset the report by emptying the report div
function resetReport() {
	reportDiv = document.getElementById('report');
	while(reportDiv.firstChild) {
	    reportDiv.removeChild(reportDiv.firstChild);
	}
}