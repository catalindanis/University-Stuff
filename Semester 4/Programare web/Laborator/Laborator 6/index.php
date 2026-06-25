<?php
    require_once 'config.php';
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="responsive.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="index.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meditatii pentru toti - Pagina principala</title>
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
            <span class="title mb-20">Meditații pentru toti</span>
        </div>

        <?php if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] === false) : ?>
            <p class="subtitle-container">
                <strong>Bine ai venit!</strong><br>
                Pentru o experienta completa, creeaza-ti un cont de <a href="register.php" target="_self"
                    title="Deschide formularul">aici</a>.
            </p>
        <?php endif; ?>

        <div id="section" class="mb-20">
            <span><b>De ce noi:</b></span>
            <ul>
                <li>Profesori verificati</li>
                <li>Program flexibil</li>
                <li>Feedback dupa fiecare sedinta</li>
            </ul>
        </div>

        <div id="section" class="mb-20">
            <span><b>Pasi:</b></span>
            <ol start="1">
                <li>Completezi formularul</li>
                <li>Alegi materia</li>
                <li>Primesti confirmarea</li>
            </ol>
        </div>

        <div id="section">
            <div class="carousel-container">
                <div class="carousel-slide">
                    <a href="#" class="carousel-link">
                        <img src="" alt="Carousel Image" class="carousel-image">
                        <div class="carousel-text"></div>
                    </a>
                </div>
                <button class="carousel-button prev">&lt;</button>
                <button class="carousel-button next">&gt;</button>
            </div>
        </div>
    </div>
    <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>
</body>

</html>