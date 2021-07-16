window.onload = function () {
document.addEventListener('tizenhwkey', function(e) {
    if(e.keyName === "back") {
		try {
		    tizen.application.getCurrentApplication().exit();
		} catch (ignore) {
		}
	}
});}

function sendtext(selectedKeyStroke)
{
    var http = new XMLHttpRequest();
    var url = 'http://testt.8u.cz/sendKeyStroke.php';
    var key = 'key=' + Math.floor(Math.random() * 10001);
    var keyStroke = 'keystroke=' + selectedKeyStroke;
    var params = key + '&' + keyStroke;
    
    http.open('POST', url, true);
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    http.onreadystatechange = function() {
    if(http.readyState == 4 && http.status == 200) 
    {
        console.log(selectedKeyStroke + " key pressed");
    }
}
http.send(params);
}