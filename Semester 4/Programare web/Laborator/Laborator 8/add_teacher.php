<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true || $_SESSION['role'] != 1) {
    header("location: login.php");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = trim($_POST["name"]);
    $subject = trim($_POST["subject"]);
    $experience = trim($_POST["experience"]);

    if (!empty($name) && !empty($subject) && is_numeric($experience)) {
        $sql = "INSERT INTO teachers (name, subject, experience) VALUES (:name, :subject, :experience)";

        if ($stmt = $pdo->prepare($sql)) {
            $stmt->bindParam(":name", $name, PDO::PARAM_STR);
            $stmt->bindParam(":subject", $subject, PDO::PARAM_STR);
            $stmt->bindParam(":experience", $experience, PDO::PARAM_INT);

            if ($stmt->execute()) {
                header("location: teachers.php");
                exit();
            } else {
                echo "Something went wrong. Please try again later.";
            }
            unset($stmt);
        }
    }
    unset($pdo);
}
?>