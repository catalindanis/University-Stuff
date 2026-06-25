<?php
require_once 'config.php';

$allowed_columns = ['name', 'subject', 'experience'];
$sort_column = isset($_GET['sortBy']) && in_array($_GET['sortBy'], $allowed_columns) ? $_GET['sortBy'] : 'name';
$sort_direction = isset($_GET['sortDir']) && in_array(strtolower($_GET['sortDir']), ['asc', 'desc']) ? $_GET['sortDir'] : 'asc';

$sql = "SELECT name, subject, experience FROM teachers ORDER BY $sort_column $sort_direction";

try {
    $stmt = $pdo->prepare($sql);
    $stmt->execute();
    $teachers = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: application/json');
    echo json_encode($teachers);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Database error: ' . $e->getMessage()]);
}
?>