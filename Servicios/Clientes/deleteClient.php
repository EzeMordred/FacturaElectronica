<?php
    include 'conexion.php';
    $cedula = $_POST['numero_documento'];
    $sqlDelete="UPDATE Entidad SET activo = 0 WHERE numero_documento = '$cedula'";

    if($mysqli->query($sqlDelete)===TRUE)
    {
        echo json_encode(array("ok"=>true));
    }else{
        echo json_encode(array("ok"=>false));
    }
    $mysqli->close();
?>