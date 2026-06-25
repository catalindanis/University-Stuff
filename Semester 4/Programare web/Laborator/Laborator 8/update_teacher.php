<?php

require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    jsonResponse(['error' => 'Acces interzis.'], 403);
    exit;
}

if ($_SESSION['role'] != 1) {
    jsonResponse(['error' => 'Acces interzis.'], 403);
    exit;
}

function jsonResponse(array $payload, int $statusCode = 200): void
{
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=UTF-8');
    echo json_encode($payload);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $input = json_decode(file_get_contents('php://input'), true);

    $id = $input['id'] ?? null;
    $name = $input['name'] ?? null;
    $subject = $input['subject'] ?? null;
    $experience = $input['experience'] ?? null;

    if (!$id || !$name || !$subject || !$experience) {
        jsonResponse(['error' => 'Toate câmpurile sunt obligatorii.'], 400);
    }

    try {
        $stmt = $pdo->prepare('UPDATE teachers SET name = ?, subject = ?, experience = ? WHERE id = ?');
        $stmt->execute([$name, $subject, $experience, $id]);

        jsonResponse(['success' => 1, 'message' => 'Profesorul a fost actualizat cu succes.']);
    } catch (PDOException $e) {
        jsonResponse(['error' => 'Eroare la actualizarea profesorului: ' . $e->getMessage()], 500);
    }
} else {
    jsonResponse(['error' => 'Metoda de cerere neacceptată.'], 405);
}

?>