<?php
require_once 'config.php';

$errors = [];
$success_message = '';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $first_name = trim($_POST['first_name']);
    $last_name = trim($_POST['last_name']);
    $email = trim($_POST['email']);
    $password = $_POST['password'];
    $password_confirm = $_POST['password_confirm'];
    $date_of_birth = trim($_POST['date']);
    $city_id = $_POST['oras'];
    $grade = $_POST['clase'];
    $wants_news = isset($_POST['regulament']) && $_POST['regulament'] === 'da' ? 1 : 0;
    $privacy_policy = isset($_POST['terms']) && $_POST['terms'] === 'da' ? 1 : 0;

    if (empty($first_name)) $errors[] = 'Prenumele este obligatoriu.';
    if (empty($last_name)) $errors[] = 'Numele este obligatoriu.';
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) $errors[] = 'Adresa de email nu este validă.';
    if (empty($city_id)) $errors[] = 'Selectarea orașului este obligatorie.';
    if (strlen($password) < 6) $errors[] = 'Parola trebuie să aibă minim 6 caractere.';
    if ($password !== $password_confirm) $errors[] = 'Parolele nu se potrivesc.';

    $stmt = $pdo->prepare('SELECT COUNT(*) FROM users WHERE email = ?');
    $stmt->execute([$email]);
    $email_exists = $stmt->fetchColumn();
    if ($email_exists > 0) {
        $errors[] = 'Un cont cu acest email există deja.';
    }

    if (empty($errors)) {
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        $role = 'student';

        $stmt = $pdo->prepare(
            'INSERT INTO users (first_name, last_name, email, password, role, date_of_birth, grade, wants_news, privacy_policy, city_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'
        );
        
        try {
            $stmt->execute([$first_name, $last_name, $email, $hashed_password, $role, $date_of_birth, $grade, $wants_news, $privacy_policy, $city_id]);
            $success_message = 'Contul a fost creat cu succes! Acum te poți autentifica.';
            
            $stmt_log = $pdo->prepare("INSERT INTO logs (action, email, status) VALUES ('register', ?, 'success')");
            $stmt_log->execute([$email]);
        } catch (PDOException $e) {
            $errors[] = 'Ceva nu a funcționat. Te rugăm să încerci din nou. (' . $e->getMessage() . ')';
        }
    }
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
    <div class="container center">
        <div class="title-container mb-10">
            <span class="title">Formular de inscriere</span>
        </div>

        <div class="subtitle-container mb-20">
            <span>Ai deja un cont? <a href = "login.php">Click aici</a></span>
        </div>

        <div class="container text-center">
            <?php if (!empty($success_message)): ?>
                <div class="success-container">
                    <p class="success"><?php echo htmlspecialchars($success_message); ?></p>
                </div>
            <?php endif; ?>
        </div>

        <form name="formular_inscriere" class="form" id="registerForm" method="post" action="register.php">
        <fieldset>
            <legend>Date personale</legend>

            <p>
                Nume:<br>
                <input id="lastNameInput" type="text" name="last_name" maxlength="40" size="30" placeholder="Nume">
            </p>

            <p>
                Prenume:<br>
                <input id="firstNameInput" type="text" name="first_name" maxlength="40" size="30" placeholder="Prenume">
            </p>

            <p>
                E-mail:<br>
                <input id="emailInput" type="text" name="email" placeholder="exemplu@student.ro" size="30">
            </p>

            <p>
                Parolă:<br>
                <input id="passwordInput" type="password" name="password" placeholder="Parolă">
            </p>

            <p>
                Confirmă Parola:<br>
                <input id="passwordConfirmInput" type="password" name="password_confirm" placeholder="Confirmă Parola">
            </p>

            <p>
                Data nașterii:<br>
                <input id="dateInput" type="date" name="date">
            </p>

            <p>
                Județ:<br>
                <select id="countySelect" name="judet">
                    <option value="">Alegeți un județ</option>
                    <?php
                    $stmt = $pdo->query('SELECT id, name FROM counties ORDER BY name');
                    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                        echo '<option value="' . $row['id'] . '">' . htmlspecialchars($row['name']) . '</option>';
                    }
                    ?>
                </select>
            </p>

            <p>
                Oraș:<br>
                <select id="citySelect" name="oras" disabled="disabled">
                    <option value="">Alegeți un județ mai întâi</option>
                </select>
            </p>

            <p>
                Clasa:<br>
                <select name="clase" multiple="multiple" size="4" id="classSelect">
                    <option value="9">IX</option>
                    <option value="10">X</option>
                    <option value="11">XI</option>
                    <option value="12">XII</option>
                </select>
            </p>

            <p>
                <input type="checkbox" name="regulament" value="da">
                Doresc noutăți:<br>
            </p>

            <p>
                <input type="checkbox" name="terms" value="da" id="termsInput">
                Sunt de acord cu prelucrarea datelor:
            </p>

            <div class="container text-center mb-20">
                <?php if (!empty($errors)): ?>
                    <div class="error-container">
                        <?php foreach ($errors as $error): ?>
                            <p class="error"><?php echo htmlspecialchars($error); ?></p>
                        <?php endforeach; ?>
                    </div>
                <?php endif; ?>
            </div>

            <div class="container">
                <input type="submit" name="trimite" value="Register" class="mb-10">
                <input type="reset" name="reseteaza" value="Resetează">
            </div>
        </fieldset>
    </form>
    </div>
    <button id="theme-toggle" class="sticky-theme-toggle">Schimbă Tema</button>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="index.js"></script>
</body>

</html>