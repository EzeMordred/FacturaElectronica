<?php
    header('Content-Type: application/json');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Authorization');
    header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
    include 'conexion.php';
    $cedula = $_POST['numero_documento'];
    $nombre = $_POST['primer_nombre'];
    $nombre2 = $_POST['segundo_nombre'];
    $apellido = $_POST['apellido_paterno'];
    $apellido2 = $_POST['apellido_materno'];
    $telefono = $_POST['telefono'];
    $direccion = $_POST['direccion'];
    $sqlInsert = "INSERT INTO Entidad(numero_documento,primer_nombre,segundo_nombre,apellido_paterno,apellido_materno,telefono,direccion) VALUES('$cedula','$nombre','$nombre2','$apellido','$apellido2','$telefono','$direccion')";
    try {
        if($mysqli->query($sqlInsert)===TRUE){
            echo json_encode(array('ok'=>true));
        }else{
            echo json_encode(array('ok'=>false, "errorMsg"=>$sqlInsert . $mysqli->error));
        }
    } catch (\Throwable $th) {
        echo json_encode(array('ok'=>false, "errorMsg"=>$th));
    } 
?>