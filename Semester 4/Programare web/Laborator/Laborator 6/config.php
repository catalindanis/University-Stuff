<?php

session_start();

define('DB_HOST', 'localhost');
define('DB_USERNAME', 'root');
define('DB_PASSWORD', '');
define('DB_NAME', 'mpt');

try {
    $pdo = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USERNAME, DB_PASSWORD);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e){
    die("EROARE: Nu s-a putut realiza conexiunea la baza de date. " . $e->getMessage());
}

try {
    $sqlite_pdo = new PDO("sqlite:" . __DIR__ . "/logs.sqlite");
    $sqlite_pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sqlite_pdo->exec("CREATE TABLE IF NOT EXISTS logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        action TEXT NOT NULL,
        email TEXT NOT NULL,
        status TEXT NOT NULL,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
    )");
} catch(PDOException $e) {
    die("EROARE: Nu s-a putut realiza conexiunea la baza de date SQLite. " . $e->getMessage());
}

if (!isset($_SESSION["loggedin"]) && isset($_COOKIE['remember_me'])) {
    list($userId, $token) = explode(':', $_COOKIE['remember_me'], 2);

    $sql = 'SELECT id, email, role FROM users WHERE id = :id AND remember_token = :token';
    
    if ($stmt = $pdo->prepare($sql)) {
        $stmt->bindParam(':id', $userId, PDO::PARAM_INT);
        $stmt->bindParam(':token', $token, PDO::PARAM_STR);
        
        if ($stmt->execute()) {
            if ($stmt->rowCount() == 1) {
                if ($user = $stmt->fetch()) {
                    $_SESSION["loggedin"] = true;
                    $_SESSION["id"] = $user['id'];
                    $_SESSION["email"] = $user['email'];
                    $_SESSION["role"] = $user['role'];
                }
            }
        }
        unset($stmt);
    }
}
?>