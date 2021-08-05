<?php
    include('db_connection.php');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
    header('Content-Type: application/json');
    
    $sql_select = 'SELECT * FROM KeyValues WHERE id = 1';
    $result = mysqli_query($conn, $sql_select);
    $keyValuesArray = mysqli_fetch_all($result, MYSQLI_ASSOC);
    mysqli_free_result($result);
    echo json_encode($keyValuesArray);
?>