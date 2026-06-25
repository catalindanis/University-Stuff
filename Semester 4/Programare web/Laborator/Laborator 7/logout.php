<?php
session_start();
require_once 'config.php';

if (isset($_SESSION["email"])) {
    $stmt_log = $pdo->prepare("INSERT INTO logs (action, email, status) VALUES ('logout', ?, 'success')");
    $stmt_log->execute([$_SESSION["email"]]);
}

$_SESSION = array();
session_destroy();

setcookie('remember_me', '', time() - 3600, "/");

header("location: index.php");
exit;
?>