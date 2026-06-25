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
    $stmt = $pdo->prepare('SELECT id, name, subject, experience FROM teachers');

    $stmt->execute();
    $teachers = $stmt->fetchAll(PDO::FETCH_ASSOC);

    jsonResponse([
        'teachers' => $teachers
    ]);
} catch (PDOException $e) {
    jsonResponse(['error' => 'Database error: ' . $e->getMessage()], 500);
}
?>