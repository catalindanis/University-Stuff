<?php
require_once 'config.php';

function xmlResponse(DOMDocument $dom, int $statusCode = 200): void
{
    http_response_code($statusCode);
    header('Content-Type: application/xml; charset=UTF-8');
    echo $dom->saveXML();
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

    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->formatOutput = true;

    $root = $dom->createElement('response');
    $dom->appendChild($root);

    $teachersNode = $dom->createElement('teachers');
    foreach ($teachers as $t) {
        $teacherNode = $dom->createElement('teacher');

        $idNode = $dom->createElement('id');
        $idNode->appendChild($dom->createTextNode((string) $t['id']));
        $teacherNode->appendChild($idNode);

        $nameNode = $dom->createElement('name');
        $nameNode->appendChild($dom->createTextNode($t['name']));
        $teacherNode->appendChild($nameNode);

        $subjectNode = $dom->createElement('subject');
        $subjectNode->appendChild($dom->createTextNode($t['subject']));
        $teacherNode->appendChild($subjectNode);

        $expNode = $dom->createElement('experience');
        $expNode->appendChild($dom->createTextNode((string) $t['experience']));
        $teacherNode->appendChild($expNode);

        $teachersNode->appendChild($teacherNode);
    }
    $root->appendChild($teachersNode);

    $paginationNode = $dom->createElement('pagination');

    $pageNode = $dom->createElement('page');
    $pageNode->appendChild($dom->createTextNode((string) $page));
    $paginationNode->appendChild($pageNode);

    $pageSizeNode = $dom->createElement('pageSize');
    $pageSizeNode->appendChild($dom->createTextNode((string) $pageSize));
    $paginationNode->appendChild($pageSizeNode);

    $totalRecordsNode = $dom->createElement('totalRecords');
    $totalRecordsNode->appendChild($dom->createTextNode((string) $totalRecords));
    $paginationNode->appendChild($totalRecordsNode);

    $totalPagesNode = $dom->createElement('totalPages');
    $totalPagesNode->appendChild($dom->createTextNode((string) $totalPages));
    $paginationNode->appendChild($totalPagesNode);

    $root->appendChild($paginationNode);

    xmlResponse($dom, 200);
} catch (PDOException $e) {
    $dom = new DOMDocument('1.0', 'UTF-8');
    $root = $dom->createElement('response');
    $dom->appendChild($root);
    $errorNode = $dom->createElement('error');
    $errorNode->appendChild($dom->createTextNode('Database error: ' . $e->getMessage()));
    $root->appendChild($errorNode);
    xmlResponse($dom, 500);
}
