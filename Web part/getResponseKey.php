<?php 
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
    
    $file = "file.txt";
    $fileText = fopen($file, "rt");
    $params = fgets($fileText);
    $paramsSplit = explode(" ", $params);
    echo $paramsSplit[2];
?>