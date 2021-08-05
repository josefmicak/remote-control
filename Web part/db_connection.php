<?php
    $conn = mysqli_connect('SERVER', 'USERNAME', 'PASSWORD', 'DATABASE NAME');
    
    if(!$conn)
    {
        echo 'Connection error: ' . mysqli_connect_error();
    }
?>