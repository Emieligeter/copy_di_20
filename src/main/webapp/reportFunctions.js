var textInput = '';
function chartToReport() {
	console.log("lets add this chart to the report.");
	var elementCopy;
	if(chartType == 'textElement') {
		var editor = $('#textFrame').contents().find('#editor');
		elementCopy = copyText(editor.cleanHtml());
		editor.clearHtml();

	} else {
		elementCopy = copyCanvas(document.getElementById('reportElement'));
	}
	document.getElementById('report').appendChild(elementCopy);
	console.log("done.");
}

function copyText(original) {
	var text =  document.createElement('p');
	text.innerHTML = original;
	return text;
}
function copyCanvas(original) {
	var imgURL = original.toDataURL("image/png");
	var img = document.createElement('img')
	img.src = imgURL;
	img.width = 200;
	img.height = 300;
	return img;
}

var printDivCSS = new String ('<link href="styles.css" rel="stylesheet" type="text/css">');
function printDiv(divId) {
    window.frames["print_frame"].document.body.innerHTML=printDivCSS + document.getElementById(divId).innerHTML;
    window.frames["print_frame"].window.focus();
    window.frames["print_frame"].window.print();
}