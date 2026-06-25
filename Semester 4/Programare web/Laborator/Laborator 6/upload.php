<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES["profile_picture"])) {
    $file = $_FILES["profile_picture"];

    if ($file["error"] === UPLOAD_ERR_OK) {
        $allowed_types = ['image/jpeg', 'image/png', 'image/gif'];
        if (in_array($file['type'], $allowed_types) && $file['size'] < 5 * 1024 * 1024) { // Limită de 5MB
            
            $stmt = $pdo->prepare("SELECT profile_picture FROM users WHERE id = :id");
            $stmt->execute(['id' => $_SESSION['id']]);
            $old_picture = $stmt->fetchColumn();

            if ($old_picture && file_exists($old_picture) && is_file($old_picture)) {
                unlink($old_picture);
            }

            $upload_dir = 'uploads/';
            if (!is_dir($upload_dir)) {
                mkdir($upload_dir, 0755, true);
            }

            $file_extension = pathinfo($file["name"], PATHINFO_EXTENSION);
            $new_filename = $upload_dir . 'user_' . $_SESSION['id'] . '_' . time() . '.' . $file_extension;

            if (move_uploaded_file($file["tmp_name"], $new_filename)) {
                $stmt = $pdo->prepare("UPDATE users SET profile_picture = :profile_picture WHERE id = :id");
                $stmt->execute([
                    'profile_picture' => $new_filename,
                    'id' => $_SESSION['id']
                ]);

                header("location: profile.php");
                exit;
            } else {
                echo "Eroare la mutarea fișierului pe server.";
            }
        } else {
            echo "Eroare: Fișierul este prea mare sau nu este un format de imagine acceptat (JPEG, PNG, GIF).";
        }
    } else {
        echo "Eroare la încărcarea fișierului.";
    }
} else {
    header("location: profile.php");
    exit;
}
