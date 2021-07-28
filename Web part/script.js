var responseKey = "0";

function sendText(selectedKeyStroke)
{
    var http = new XMLHttpRequest();
    var url = 'sendKeyStroke.php';
    var type = 'type=1';
    var messageKey = 'messageKey=' + Math.floor(Math.random() * 10001);
    var keyStroke = 'keyStroke=' + selectedKeyStroke;
    var params = type + '&' + messageKey + '&' + keyStroke;    

    responseKey = getResponseKey();

    http.open('POST', url, true);
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    http.onreadystatechange = function()
    {
        if(http.readyState == 4 && http.status == 200)
        {
            controlIndicatorLight(1, 'y');
            console.log(selectedKeyStroke + " key pressed");
        }
        else
        {
            controlIndicatorLight(1, 'n');
        }
    }
    http.send(params);

    setTimeout(function()
    {
        var newResponseKey = getResponseKey();
        if(newResponseKey != responseKey)
        {
            controlIndicatorLight(2, 'y');
        }
        else
        {
            controlIndicatorLight(2, 'n')
        }
    }, 1500);
}

function getResponseKey()
{
    var r = new XMLHttpRequest();
    r.open('GET', 'getResponseKey.php', false);
    r.send(null); 
    if (r.status == 200)
    {
    	var responseKeyToReturn = 0;
    	var responseKeyChars = r.responseText.slice(-5);
    	var responseKeySplit = responseKeyChars.split(">");
    	if(responseKeySplit[1] == null)
    	{
    		responseKeyToReturn = responseKeySplit[0];
    	}
    	else
    	{
    		responseKeyToReturn = responseKeySplit[1];
    	}
    }
    return responseKeyToReturn;
}

function controlIndicatorLight(lightId, success)
{
    if(lightId == 1)
    {
        if(success == 'y')
	    {
	        document.getElementById("messageIndicatorLight").style.backgroundColor = "limegreen";
	    }
	    else
	    {
	        document.getElementById("messageIndicatorLight").style.backgroundColor = "red";
	    }
        setTimeout(function()
        {
            document.getElementById("messageIndicatorLight").style.backgroundColor = "cyan";
        }, 2000);
    }
    else
    {
        if(success == 'y')
	    {
	        document.getElementById("responseIndicatorLight").style.backgroundColor = "limegreen";
	    }
	    else
	    {
	        document.getElementById("responseIndicatorLight").style.backgroundColor = "red";
	    }
        setTimeout(function()
        {
            document.getElementById("responseIndicatorLight").style.backgroundColor = "cyan";
        }, 2000);
    }
}