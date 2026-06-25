<?php
require_once 'config.php';

function jsonResponse(array $payload, int $statusCode = 200): void
{
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=UTF-8');
    echo json_encode($payload);
    exit;
}

$page = filter_input(INPUT_GET, 'page', FILTER_VALIDATE_INT, [
    'options' => ['default' => 1, 'min_range' => 1]
]);

$pageSize = filter_input(INPUT_GET, 'pageSize', FILTER_VALIDATE_INT, [
    'options' => ['default' => 10, 'min_range' => 1]
]);

if ($page === false || $page === null) {
    $page = 1;
}

try {
    if ($pageSize === false || $pageSize === null) {
        $pageSize = 5;
    }

    $countStmt = $pdo->query('SELECT COUNT(*) AS total FROM teachers');
    $totalRecords = (int) $countStmt->fetch(PDO::FETCH_ASSOC)['total'];
    $totalPages = max(1, (int) ceil($totalRecords / $pageSize));

    if ($page > $totalPages) {
        $page = $totalPages;
    }

    $offset = ($page - 1) * $pageSize;

    $stmt = $pdo->prepare('SELECT id, name, subject, experience FROM teachers ORDER BY id ASC LIMIT :limit OFFSET :offset');
    $stmt->bindValue(':limit', $pageSize, PDO::PARAM_INT);
    $stmt->bindValue(':offset', $offset, PDO::PARAM_INT);

    $stmt->execute();
    $teachers = $stmt->fetchAll(PDO::FETCH_ASSOC);

    jsonResponse([
        'teachers' => $teachers,
        'pagination' => [
            'page' => $page,
            'pageSize' => $pageSize,
            'totalRecords' => $totalRecords,
            'totalPages' => $totalPages
        ]
    ]);
} catch (PDOException $e) {
    jsonResponse(['error' => 'Database error: ' . $e->getMessage()], 500);
}