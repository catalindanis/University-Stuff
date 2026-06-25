<?php

session_start();

try {
    $pdo = new PDO("sqlite:" . __DIR__ . "/database.sqlite");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->exec("PRAGMA foreign_keys = ON;");

    $pdo->exec("CREATE TABLE IF NOT EXISTS counties (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT
    )");

    $pdo->exec("CREATE TABLE IF NOT EXISTS cities (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        county_id INTEGER,
        FOREIGN KEY (county_id) REFERENCES counties (id) ON DELETE CASCADE ON UPDATE CASCADE
    )");

    $pdo->exec("CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        first_name TEXT,
        last_name TEXT,
        email TEXT,
        password TEXT,
        date_of_birth DATE,
        city_id INTEGER,
        grade INTEGER,
        wants_news INTEGER,
        privacy_policy INTEGER,
        role INTEGER,
        remember_token TEXT,
        profile_picture TEXT,
        FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE SET NULL ON UPDATE CASCADE
    )");

    $pdo->exec("CREATE TABLE IF NOT EXISTS teachers (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        subject TEXT,
        experience INTEGER
    )");

    $pdo->exec("CREATE TABLE IF NOT EXISTS logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        action TEXT NOT NULL,
        email TEXT NOT NULL,
        status TEXT NOT NULL,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
    )");
} catch(PDOException $e) {
    die("EROARE: Nu s-a putut realiza conexiunea la baza de date. " . $e->getMessage());
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