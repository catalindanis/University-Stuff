<?php
require_once 'config.php';

function jsonResponse(array $payload, int $statusCode = 200): void
{
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=UTF-8');
    echo json_encode($payload);
    exit;
}

try {
    if (isset($_GET['teacher_id'])) {
        $teacher_id = $_GET['teacher_id'];
        $stmt = $pdo->prepare('SELECT id, name, subject, experience FROM teachers WHERE id = ?');
        $stmt->execute([$teacher_id]);
        $teacher = $stmt->fetch(PDO::FETCH_ASSOC);

        jsonResponse([
            'teacher' => $teacher
        ]);
    }

    jsonResponse(['error' => 'Id-ul profesorului este necesar.'], 400);
} catch (PDOException $e) {
    jsonResponse(['error' => 'Database error: ' . $e->getMessage()], 500);
}
?>