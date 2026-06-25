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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meditații pentru toți - Profesori 1</title>
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
            <span class="title">Profesori - Cerința 5</span>
        </div>

        <div id="section">
            <h2>Editare profesori vanilla</h2>

            <div class="pagination-controls">
                Id profesor:<br>
                <select id="teacherSelect" name="teacher">
                </select>

                Nume profesor:<br>
                <input type="text" id="teacherName" name="teacherName">

                Materie profesor:<br>
                <input type="text" id="teacherSubject" name="teacherSubject">

                Experienta profesor:<br>
                <input type="text" id="teacherExperience" name="teacherExperience">

                <button type="button" class="button" id="updateTeacherButton">Salveaza</button>
            </div>

            <!-- <div class="pagination-controls">
                <div class="page-size-group">
                    <label for="page-size">Număr înregistrări pe pagină</label>
                    <input type="number" id="page-size" min="1" value="10">
                    <button type="button" id="apply-page-size">Aplică</button>
                </div>

                <div class="pagination-actions">
                    <button type="button" id="previous-page">Previous k</button>
                    <button type="button" id="next-page">Next k</button>
                </div>
            </div>

            <div class="table-responsive">
                <table id="teachers-table">
                    <thead>
                        <tr>
                            <th>Nume</th>
                            <th>Materie</th>
                            <th>Experiență (ani)</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div> -->
        </div>
    </div>

    <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>
    <script src="edit1.js"></script>
</body>

</html>