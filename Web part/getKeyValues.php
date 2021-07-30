<?php
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
    header('Content-Type: application/json');
    
    $file = "file.txt";
    $fileText = fopen($file, "rt");
    $params = fgets($fileText);
    $paramsSplit = explode(" ", $params);
    $arr = array('messageKey' => $paramsSplit[0], 'keyStroke' => $paramsSplit[1], 'responseKey' => $paramsSplit[2]);
    echo json_encode($arr);
?>