<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}

if ($_SESSION['role'] != 1) {
    header("location: index.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="responsive.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="index.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meditații pentru toți - Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
    <nav class="container">
        <ul class="navbar">
            <li><a href="index.php">Acasă</a></li>
            <?php if (isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true) : ?>
                <?php if ($_SESSION['role'] == 1) : ?>
                    <li><a href="dashboard.php">Dashboard</a></li>
                <?php endif; ?>
                <li><a href="profile.php">Profil</a></li>
                <li class="has-submenu">
                    <a href="#">Despre</a>
                    <ul class="submenu">
                        <li><a href="contact.php">Contact</a></li>
                        <li><a href="teachers.php">Profesori</a></li>
                    </ul>
                </li>
                <li><a href="logout.php">Deconectare</a></li>
            <?php else : ?>
                <li><a href="register.php">Înscriere</a></li>
            <?php endif; ?>
        </ul>
    </nav>
    <div class="container content">
        <div class="title-container">
            <span class="title">Dashboard</span>
        </div>
        <div class="widgets-container">
            <div class="card">
                <div class="card-title">Total utilizatori</div>
                <div class="card-content">120</div>
            </div>
            <div class="card">
                <div class="card-title">Meditații active</div>
                <div class="card-content">15</div>
            </div>
            <div class="card">
                <div class="card-title">Programări azi</div>
                <div class="card-content">8</div>
            </div>
            <div class="card">
                <div class="card-title">Activitate recentă</div>
                <div class="card-content">
                    <ul class="collapsible-list">
                        <li class="expandable">
                            <span>Utilizator nou înregistrat</span>
                            <ul>
                                <li class="expandable">
                                    <span>Programare nouă</span>
                                    <ul>
                                    <li>Materie: Matematică</li>
                                    <li>Profesor: Ionescu Ana</li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li class="expandable">
                            <span>Programare nouă</span>
                            <ul>
                                <li>Materie: Matematică</li>
                                <li>Profesor: Ionescu Ana</li>
                            </ul>
                        </li>
                        <li class="expandable">
                            <span>Plată confirmată</span>
                            <ul>
                                <li>Suma: 100 RON</li>
                                <li>Metoda: Card</li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
        <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>

</body>

</html>