<?php
require_once 'config.php';

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
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

if ($pageSize === false || $pageSize === null) {
    $pageSize = 10;
}

try {
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
} catch (PDOException $e) {
    $teachers = [];
    $totalRecords = 0;
    $totalPages = 1;
    $page = 1;
    $pageSize = 10;
    $errorMessage = 'Eroare la încărcarea profesorilor.';
}

if (!isset($errorMessage)) {
    $errorMessage = '';
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="responsive.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meditații pentru toți - Profesori 4</title>
</head>

<body>
    <nav class="container">
        <ul class="navbar">
            <li><a href="index.php">Acasă</a></li>
            <?php if (isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true) : ?>
                <?php if ($_SESSION['role'] == 1) : ?>
                    <li><a href="dashboard.php">Dashboard</a></li>
                <?php endif; ?>
                <li><a href="profile.php">Profil</a></li>
                <li class="has-submenu">
                    <a href="#">Despre</a>
                    <ul class="submenu">
                        <li><a href="contact.php">Contact</a></li>
                        <li><a href="teachers.php">Profesori</a></li>
                    </ul>
                </li>
                <li><a href="logout.php">Deconectare</a></li>
            <?php else : ?>
                <li><a href="register.php">Înscriere</a></li>
            <?php endif; ?>
        </ul>
    </nav>

    <div class="container content">
        <div class="title-container">
            <span class="title">Profesori - Cerința 4</span>
        </div>

        <div id="section">
            <h2>Listă de profesori cu paginare server-side</h2>

            <div class="pagination-controls">
                <form class="page-size-group" method="get" action="teachers4.php">
                    <label for="page-size">Număr înregistrări pe pagină</label>
                    <input type="number" id="page-size" name="pageSize" min="1" value="<?php echo htmlspecialchars((string) $pageSize, ENT_QUOTES, 'UTF-8'); ?>">
                    <input type="hidden" name="page" value="1">
                    <button type="submit">Aplică</button>
                </form>

                <div class="pagination-actions">
                    <form method="get" action="teachers4.php">
                        <input type="hidden" name="pageSize" value="<?php echo htmlspecialchars((string) $pageSize, ENT_QUOTES, 'UTF-8'); ?>">
                        <input type="hidden" name="page" value="<?php echo htmlspecialchars((string) max(1, $page - 1), ENT_QUOTES, 'UTF-8'); ?>">
                        <button type="submit" <?php echo $page <= 1 ? 'disabled' : ''; ?>>Previous k</button>
                    </form>

                    <form method="get" action="teachers4.php">
                        <input type="hidden" name="pageSize" value="<?php echo htmlspecialchars((string) $pageSize, ENT_QUOTES, 'UTF-8'); ?>">
                        <input type="hidden" name="page" value="<?php echo htmlspecialchars((string) min($totalPages, $page + 1), ENT_QUOTES, 'UTF-8'); ?>">
                        <button type="submit" <?php echo $page >= $totalPages ? 'disabled' : ''; ?>>Next k</button>
                    </form>
                </div>
            </div>

            <div class="table-responsive">
                <table id="teachers-table">
                    <thead>
                        <tr>
                            <th>Nume</th>
                            <th>Materie</th>
                            <th>Experiență (ani)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php if ($errorMessage !== '') : ?>
                            <tr>
                                <td colspan="3" class="text-center error"><?php echo htmlspecialchars($errorMessage, ENT_QUOTES, 'UTF-8'); ?></td>
                            </tr>
                        <?php elseif (empty($teachers)) : ?>
                            <tr>
                                <td colspan="3" class="text-center">Nu sunt profesori de afișat.</td>
                            </tr>
                        <?php else : ?>
                            <?php foreach ($teachers as $teacher) : ?>
                                <tr>
                                    <td data-label="Nume"><?php echo htmlspecialchars($teacher['name'] ?? '', ENT_QUOTES, 'UTF-8'); ?></td>
                                    <td data-label="Materie"><?php echo htmlspecialchars($teacher['subject'] ?? '', ENT_QUOTES, 'UTF-8'); ?></td>
                                    <td data-label="Experiență (ani)"><?php echo htmlspecialchars((string) ($teacher['experience'] ?? ''), ENT_QUOTES, 'UTF-8'); ?></td>
                                </tr>
                            <?php endforeach; ?>
                        <?php endif; ?>
                    </tbody>
                </table>
            </div>

            <div id="page-info" class="pagination-info">
                Pagina <?php echo htmlspecialchars((string) $page, ENT_QUOTES, 'UTF-8'); ?> din <?php echo htmlspecialchars((string) $totalPages, ENT_QUOTES, 'UTF-8'); ?>
                | <?php echo htmlspecialchars((string) $totalRecords, ENT_QUOTES, 'UTF-8'); ?> înregistrări
            </div>
        </div>
    </div>
</body>

</html>
