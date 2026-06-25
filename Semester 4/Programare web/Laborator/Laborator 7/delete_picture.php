<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $stmt = $pdo->prepare("SELECT profile_picture FROM users WHERE id = :id");
    $stmt->execute(['id' => $_SESSION['id']]);
    $old_picture = $stmt->fetchColumn();

    if ($old_picture && file_exists($old_picture) && is_file($old_picture)) {
        unlink($old_picture);
    }

    $stmt = $pdo->prepare("UPDATE users SET profile_picture = NULL WHERE id = :id");
    $stmt->execute(['id' => $_SESSION['id']]);
}

header("location: profile.php");
exit;
