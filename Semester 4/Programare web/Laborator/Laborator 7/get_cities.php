<?php
require_once 'config.php';

$cities = [];
if (isset($_GET['county_id'])) {
    $county_id = $_GET['county_id'];
    $stmt = $pdo->prepare('SELECT id, name FROM cities WHERE county_id = ? ORDER BY name');
    $stmt->execute([$county_id]);
    $cities = $stmt->fetchAll(PDO::FETCH_ASSOC);
}

header('Content-Type: application/json');
echo json_encode($cities);
?>
