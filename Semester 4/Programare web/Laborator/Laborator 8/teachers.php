<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="responsive.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meditații pentru toți - Profesori</title>
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
            <span class="title">Profesori</span>
        </div>
        <div id="section">
            <?php if ($_SESSION['role'] == 1) : ?>
                <div id="add-teacher-container">
                    <h2>Adaugă un profesor nou</h2>
                    <form action="add_teacher.php" method="post" class="add-teacher-form">
                        <input type="text" id="name" name="name" placeholder="Nume" required>
                        <input type="text" id="subject" name="subject" placeholder="Materie" required>
                        <input type="number" id="experience" name="experience" placeholder="Experiență (ani)" required>
                        <input type="submit" value="Adauga">
                    </form>
                    <hr>
                </div>
            <?php endif; ?>
            <h2>Lista de profesori</h2>
            <table id="teachers-table">
                <thead>
                    <tr>
                        <th data-column="name">Nume<span class="sort-indicator"></span></th>
                        <th data-column="subject">Materie<span class="sort-indicator"></span></th>
                        <th data-column="experience">Experiență (ani)<span class="sort-indicator"></span></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <hr>
        </div>
    </div>    
        <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="index.js"></script>
</body>

</html>