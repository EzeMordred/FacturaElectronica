<?php

$serverName = "34.134.141.129";
$userName = "root";
$password = "12345678";
$dbName = "facturacion";

$conn = mysqli_connect($serverName, $userName, $password, $dbName);
$mysqli = new mysqli($serverName, $userName, $password, $dbName);

if(!$mysqli){
    die("Error: ".mysqli_connect_error());
}else{
    echo "Conexion exitosa";
}

?>