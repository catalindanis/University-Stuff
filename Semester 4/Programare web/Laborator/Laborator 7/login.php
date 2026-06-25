<?php
require_once 'config.php';

if (isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true) {
    header("location: dashboard.php");
    exit;
}

$error = '';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    if (empty($_POST['captcha']) || strtolower($_POST['captcha']) != strtolower($_SESSION['captcha_text'])) {
        $error = 'Codul CAPTCHA introdus este incorect.';
    } else {
        $email = trim($_POST['email']);
        $password = $_POST['password'];

        if (empty($email) || empty($password)) {
            $error = 'Te rugăm să completezi atât email-ul, cât și parola.';
        } else {
            $sql = 'SELECT id, email, password, role FROM users WHERE email = :email';
            
            if ($stmt = $pdo->prepare($sql)) {
                $stmt->bindParam(':email', $email, PDO::PARAM_STR);
                
                if ($stmt->execute()) {
                    if ($stmt->rowCount() == 1) {
                        if ($user = $stmt->fetch()) {
                            if (password_verify($password, $user['password'])) {
                                if (isset($_POST['remember'])) {
                                    $token = bin2hex(random_bytes(16));
                                    $userId = $user['id'];
                                    $pdotoken = $pdo->prepare('UPDATE users SET remember_token = :token WHERE id = :id');
                                    $pdotoken->bindParam(':token', $token);
                                    $pdotoken->bindParam(':id', $userId);
                                    $pdotoken->execute();
                                    setcookie('remember_me', $userId . ':' . $token, time() + (86400 * 30), "/"); // 30 days
                                }
                                $_SESSION["loggedin"] = true;
                                $_SESSION["id"] = $user['id'];
                                $_SESSION["email"] = $user['email'];
                                $_SESSION["role"] = $user['role'];
                                
                                $stmt_log = $pdo->prepare("INSERT INTO logs (action, email, status) VALUES ('login', ?, 'success')");
                                $stmt_log->execute([$email]);
                                
                                header("location: index.php");
                                exit;
                            } else {
                                $stmt_log = $pdo->prepare("INSERT INTO logs (action, email, status) VALUES ('login', ?, 'failed')");
                                $stmt_log->execute([$email]);
                                $error = 'Parola introdusă nu este corectă.';
                            }
                        }
                    } else {
                        $stmt_log = $pdo->prepare("INSERT INTO logs (action, email, status) VALUES ('login', ?, 'failed')");
                        $stmt_log->execute([$email]);
                        $error = 'Emailul introdus este incorect.';
                    }
                } else {
                    $error = 'A apărut o eroare. Încearcă din nou.';
                }

                unset($stmt);
            }
        }
    }
    unset($_SESSION['captcha_text']);
}
?>

<!DOCTYPE HTML>
<html lang="en">

<head>
    <link rel="stylesheet" href="responsive.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meditații pentru toți - Înscriere</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
    <nav class="container">
        <ul class="navbar">
            <li><a href="index.php">Acasă</a></li>
            <li><a href="register.php">Înscriere</a></li>
        </ul>
    </nav>
    <div class="container v-center">
        <div class="title-container mb-10">
            <span class="title">Intră în cont</span>
        </div>

        <div class="subtitle-container mb-20">
            <span>Nu ai cont? <a href = "register.php">Click aici</a></span>
        </div>


        <form name="formular_inscriere" action="login.php" method="post" class="form" id="loginForm">
        <fieldset>
            <legend>Date personale</legend>

            <p>
                E-mail:<br>
                <input type="text" name="email" placeholder="exemplu@student.ro" size="30" id="emailInput">
            </p>
            
            <p>
                Parolă:<br>
                <input type="password" name="password" placeholder="Parolă" id="passwordInput">
            </p>

            <p>
                <img src="captcha.php" alt="CAPTCHA Image" /><br>
                <input type="text" name="captcha" placeholder="Introdu textul din imagine" size="30">
            </p>

            <p>
                <input type="checkbox" name="remember" id="remember">
                <label for="remember">Ține-mă minte</label>
            </p>

            <div class="container text-center mb-20">
                <span class="error" id="registerErrorText"><?php echo $error; ?></span>
            </div>

            <div class="container">
                <input type="submit" name="trimite" value="Login">
            </div>
        </fieldset>
    </form>
    </div>

        <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="index.js"></script>
</body>

</html>