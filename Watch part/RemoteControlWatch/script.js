window.onload = function () {
document.addEventListener('tizenhwkey', function(e) {
    if(e.keyName === "back") {
		try {
		    tizen.application.getCurrentApplication().exit();
		} catch (ignore) {
		}
	}
});}

function sendText(selectedKeyStroke)
{
    //the space key gets disabled for a second after pressing it because otherwise it keeps being indefinitely automatically pressed
    if(selectedKeyStroke == "space")
    {
        document.getElementById("spaceButton").disabled = true;
        setTimeout(function()
        {
            document.getElementById("spaceButton").disabled = false;
        }, 1000);
    }
    var http = new XMLHttpRequest();
    var url = 'http://testt.8u.cz/sendKeyStroke.php';
    var type = 'type=1';
    var messageKey = 'messageKey=' + Math.floor(Math.random() * 10001);
    var keyStroke = 'keyStroke=' + selectedKeyStroke;
    var params = type + '&' + messageKey + '&' + keyStroke;    

    var responseKey = 0;
    fetch('http://testt.8u.cz/getKeyValues.php')
    .then(response => 
    {
        return response.json();
    })
    .then(response =>
    {
        responseKey = response[0].responseKey;
    });

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
    };
    http.send(params);

    setTimeout(function()
    {
        var newResponseKey = 0;
        fetch('http://testt.8u.cz/getKeyValues.php')
        .then(response => 
        {
            return response.json();
        })
        .then(response =>
        {
            newResponseKey = response[0].responseKey;
        })
        .then(response =>
        {
            if(newResponseKey != responseKey)
            {
                controlIndicatorLight(2, 'y');
            }
            else
            {
                controlIndicatorLight(2, 'n');
            }
        });
    }, 1500);
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