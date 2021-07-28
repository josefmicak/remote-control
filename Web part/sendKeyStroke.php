<?php 
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
    
    $type = $_POST['type'];
    $file = "file.txt";
    $fileText = fopen($file, "rt");
    $params = fgets($fileText);
    $paramsSplit = explode(" ", $params);
    
    if($type == 1)//message from remote device
    {
        $messageKey = $_POST['messageKey'];
        $keyStroke = $_POST['keyStroke'];
        $responseKey = $paramsSplit[2];
    }
    else//response from desktop
    {
        $messageKey = $paramsSplit[0];
        $keyStroke = $paramsSplit[1];
        //$messageKey = 1;
        //$keyStroke = "left";
        $responseKey = $_POST['responseKey'];
    }

    file_put_contents($file, $messageKey . ' ' . $keyStroke . ' ' . $responseKey);
?>