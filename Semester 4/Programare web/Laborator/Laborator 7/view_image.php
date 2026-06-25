<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    http_response_code(403);
    echo "Acces interzis";
    exit;
}

$stmt = $pdo->prepare("SELECT profile_picture FROM users WHERE id = :id");
$stmt->execute(['id' => $_SESSION['id']]);
$user = $stmt->fetch();

if ($user && !empty($user['profile_picture'])) {
    $finfo = finfo_open();
    $mime_type = finfo_buffer($finfo, $user['profile_picture'], FILEINFO_MIME_TYPE);
    finfo_close($finfo);

    header("Content-Type: " . $mime_type); 
    echo $user['profile_picture'];
    exit;
} else {
    http_response_code(404);
    exit;
}
exit;
