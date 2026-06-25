<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}

$stmt = $pdo->prepare("SELECT first_name, last_name, email, profile_picture FROM users WHERE id = :id");
$stmt->execute(['id' => $_SESSION['id']]);
$user = $stmt->fetch();

$has_profile_picture = !empty($user['profile_picture']);
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profilul Meu</title>
    <link rel="stylesheet" href="responsive.css">
</head>
<body>

<header>
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
</header>

<main class="container content">
    <div class="title-container">
        <span class="title">Profilul Meu</span>
    </div>

    <div id="section">
        <p><strong>Nume:</strong> <?php echo htmlspecialchars($user['first_name']); ?></p>
        <p><strong>Prenume:</strong> <?php echo htmlspecialchars($user['last_name']); ?></p>
        <p><strong>Email:</strong> <?php echo htmlspecialchars($user['email']); ?></p>

        <hr>

        <h3>Poza de Profil</h3>
        <?php if ($has_profile_picture): ?>
            <img src="<?php echo htmlspecialchars($user['profile_picture']); ?>" alt="Poza de profil" style="max-width: 200px; height: auto; border-radius: 50%;">
            <form action="delete_picture.php" method="post" style="margin-top: 10px;">
                <button type="submit">Șterge Poza</button>
            </form>
        <?php else: ?>
            <p>Nu ai o poză de profil.</p>
            <form action="upload.php" method="post" enctype="multipart/form-data">
                <p>Selectează o imagine pentru a o încărca:</p>
                <input type="file" name="profile_picture" id="profile_picture" accept="image/jpeg, image/png, image/gif">
                <button type="submit">Încarcă</button>
            </form>
        <?php endif; ?>
    </div>
</main>

</body>
</html>
