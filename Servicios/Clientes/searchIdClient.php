<?php
    header('Content-Type: application/json');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Authorization');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
    include 'conexion.php';
    $cedula = $_POST['numero_documento'];
    $sqlSelect = "SELECT * FROM Entidad WHERE numero_documento = '$cedula'";
    $respuesta = $conn->query($sqlSelect);
    $result = array();
    if($respuesta->num_rows>0){
        while($filacliente=$respuesta->fetch_assoc()){
            array_push($result,$filacliente);
        }
    } else {
        echo json_encode(array('clientes'=>$result=[]));
    }
    echo json_encode(array('clientes' => $result));
?>