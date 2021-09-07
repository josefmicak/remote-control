<?php 
    include('db_connection.php');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
    
    $sql_select = "SELECT * FROM RCValues WHERE username = '$username'";
    $result = mysqli_query($conn, $sql_select);
    $keyValuesArray = mysqli_fetch_all($result, MYSQLI_ASSOC);
    mysqli_free_result($result);

    $type = $_POST['type'];
    if($type == 1)//message from remote device
    {
        $username = mysqli_real_escape_string($conn, $_POST['username']);
        $messageKey = mysqli_real_escape_string($conn, $_POST['messageKey']);
        $keyStroke = mysqli_real_escape_string($conn, $_POST['keyStroke']);
        $responseKey = $keyValuesArray[0]['responseKey'];
        echo "test";
    }
    else//response from desktop
    {
        $messageKey = $keyValuesArray[0]['messageKey'];
        $keyStroke = $keyValuesArray[0]['keyStroke'];
        $responseKey = mysqli_real_escape_string($conn, $_POST['responseKey']);
    }
    $sql_insert = "INSERT INTO RCValues(username, messageKey, keyStroke, responseKey)
    VALUES ('$username', '$messageKey', '$keyStroke', '$responseKey')
    ON DUPLICATE KEY UPDATE messageKey = '$messageKey', keyStroke = '$keyStroke', responseKey = '$responseKey';";
    if(!mysqli_query($conn, $sql_insert))
    {
        echo "Query error: " . mysqli_error($conn);
    }
?>