<?php
    include 'conexion.php';
    $Cedula=$_POST['numero_documento'];
    $Nombre=$_POST['primer_nombre'];
    $Nombre2=$_POST['segundo_nombre'];
    $Apellido=$_POST['apellido_paterno'];
    $Apellido2=$_POST['apellido_materno'];
    $Telefono=$_POST['telefono'];
    $Direccion=$_POST['direccion'];
    $sqlUpdate="UPDATE Entidad SET primer_nombre = ('$Nombre'), segundo_nombre = ('$Nombre2'), apellido_paterno = ('$Apellido'), apellido_materno = ('$Apellido2'), telefono = ('$Telefono'), direccion = ('$Direccion') WHERE numero_documento = ('$Cedula')";
    if($mysqli->query($sqlUpdate)===TRUE)
    {
        echo json_encode(array('ok' => true));
    }
    else
    {
        echo json_encode(array('ok' => false, 'errorMsg' => $sqlUpdate . $mysqli -> error));
    }
    $mysqli->close();
?>