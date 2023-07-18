<?php
header('Access-Control-Allow-Origin:*');
header('Access-Control-Allow-Headers: Origin, X-Requested-with, Content-type, Authorization');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');

include 'connection.php';

$user = mysqli_real_escape_string($conn, $_POST['user']);
$password = mysqli_real_escape_string($conn, $_POST['password']);

session_start();
$_SESSION['user'] = $user;

$sentencia = $conn->prepare("SELECT * FROM empleado WHERE id_empleado = ? AND clave_acceso = ?");
$sentencia->bind_param('ss', $user, $password);
$sentencia->execute();

$resultado = $sentencia->get_result();

if ($fila = $resultado->fetch_assoc()) {
    echo json_encode($fila, JSON_UNESCAPED_UNICODE);
}

mysqli_free_result($resultado);
$sentencia->close();
$conn->close();
?>