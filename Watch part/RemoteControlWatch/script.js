window.onload = function () {
document.addEventListener('tizenhwkey', function(e) {
    if(e.keyName === "back") {
		try {
		    tizen.application.getCurrentApplication().exit();
		} catch (ignore) {
		}
	}
});}

var changeableKeyButton1, changeableKeyButton2, changeableKeyButton3;
window.onload = loadUserSettings;

function sendText(selectedKeyStroke)
{
    var username = localStorage.getItem('username');
    if(null === username)
    {
        alert("Please select a username in the settings before using the remote control.");
    }
    else
    {
        if(selectedKeyStroke == "firstButton")
        {
            selectedKeyStroke = document.getElementById("changeableKeyButton1").textContent;
        }
        else if(selectedKeyStroke == "secondButton")
        {
            selectedKeyStroke = document.getElementById("changeableKeyButton2").textContent;
        }
        else if(selectedKeyStroke == "thirdButton")
        {
            selectedKeyStroke = document.getElementById("changeableKeyButton3").textContent;
        }
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
        var url = 'http://rc.8u.cz/sendKeyStroke.php';
        var type = 'type=1';
        var username = 'username=' + username;
        var messageKey = 'messageKey=' + Math.floor(Math.random() * 10001);
        var keyStroke = 'keyStroke=' + selectedKeyStroke;
        var params = type +  '&' + username + '&' + messageKey + '&' + keyStroke;
        console.log("ablabla" + params);
    
        var responseKey = 0;
        fetch('http://rc.8u.cz/getKeyValues.php')
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
            fetch('http://rc.8u.cz/getKeyValues.php')
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

function loadUserSettings()
{
    var usernameLoaded = true;
    var username = localStorage.getItem('username');
    if(null === username)
    {
        usernameLoaded = false;
    }
    var changeableKeyButton1 = localStorage.getItem('changeableKeyButton1');
    if(null === changeableKeyButton1)
    {
        changeableKeyButton1 = 'A';
    }
    var changeableKeyButton2 = localStorage.getItem('changeableKeyButton2');
    if(null === changeableKeyButton2)
    {
        changeableKeyButton2 = 'B';
    }
    var changeableKeyButton3 = localStorage.getItem('changeableKeyButton3');
    if(null === changeableKeyButton3)
    {
        changeableKeyButton3 = 'C';
    }

    if(usernameLoaded)
    {
        document.getElementById("username").textContent = username;
        document.getElementById("username").value = username;
    }
    
    document.getElementById("changeableKeyButton1").textContent = changeableKeyButton1;
    document.getElementById("changeableKeyButton2").textContent = changeableKeyButton2;
    document.getElementById("changeableKeyButton3").textContent = changeableKeyButton3;

    document.getElementById("firstChangeableKey").value = changeableKeyButton1;
    document.getElementById("secondChangeableKey").value = changeableKeyButton2;
    document.getElementById("thirdChangeableKey").value = changeableKeyButton3;
}

function saveUserSettings()
{
    localStorage.setItem('username', document.getElementById("username").value);
    tizen.preference.setValue('username', document.getElementById("username").value);
    
    localStorage.setItem('changeableKeyButton1', document.getElementById("firstChangeableKey").value);
    tizen.preference.setValue('changeableKeyButton1', document.getElementById("firstChangeableKey").value);
    localStorage.setItem('changeableKeyButton2', document.getElementById("secondChangeableKey").value);
    tizen.preference.setValue('changeableKeyButton2', document.getElementById("secondChangeableKey").value);
    localStorage.setItem('changeableKeyButton3', document.getElementById("thirdChangeableKey").value);
    tizen.preference.setValue('changeableKeyButton3', document.getElementById("thirdChangeableKey").value);
}

var aboutModal = document.getElementById("aboutModal");
var settingsModal = document.getElementById("settingsModal");

var aboutButton = document.getElementById("aboutButton");
var settingsButton = document.getElementById("settingsButton");

var aboutModalClose = document.getElementsByClassName("close")[0];
var settingsModalClose = document.getElementsByClassName("close")[1];

aboutButton.onclick = function() {
    aboutModal.style.display = "block";
};

settingsButton.onclick = function() {
    settingsModal.style.display = "block";
};

aboutModalClose.onclick = function() {
    aboutModal.style.display = "none";
};

settingsModalClose.onclick = function() {
    settingsModal.style.display = "none";
};

window.onclick = function(event) {
    if (event.target == aboutModal) {
        aboutModal.style.display = "none";
    }

    if (event.target == settingsModal) {
        settingsModal.style.display = "none";
    }
};