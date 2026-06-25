$(function () {
    const $tableBody = $('#teachers-table tbody');
    const $pageSizeInput = $('#page-size');
    const $applyBtn = $('#apply-page-size');
    const $prevBtn = $('#previous-page');
    const $nextBtn = $('#next-page');
    const $pageInfo = $('#page-info');
    const $themeToggle = $('#theme-toggle');
    const $body = $(document.body);

    if ($tableBody.length === 0 || $pageSizeInput.length === 0 || $applyBtn.length === 0 || $prevBtn.length === 0 || $nextBtn.length === 0 || $pageInfo.length === 0) {
        return;
    }

    const initialPageSize = Math.max(1, parseInt($pageSizeInput.val(), 10) || 5);
    let currentPage = 1;
    let currentPageSize = initialPageSize;
    let totalPages = 1;

    function applyTheme(theme) {
        if (theme === 'dark') {
            $body.addClass('dark-theme');
        } else {
            $body.removeClass('dark-theme');
        }
    }

    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        applyTheme(savedTheme);
    }

    function setLoading(isLoading) {
        $prevBtn.prop('disabled', isLoading);
        $nextBtn.prop('disabled', isLoading);
        $applyBtn.prop('disabled', isLoading);
        $pageSizeInput.prop('disabled', isLoading);
        if (isLoading) {
            $pageInfo.text('Se încarcă...');
        }
    }

    function renderTeachers(teachers) {
        $tableBody.empty();

        if (!teachers || teachers.length === 0) {
            const $emptyRow = $('<tr>');
            const $cell = $('<td>').attr('colspan', 3).addClass('text-center').text('Nu sunt profesori de afișat.');
            $emptyRow.append($cell);
            $tableBody.append($emptyRow);
            return;
        }

        $.each(teachers, function (i, teacher) {
            const $row = $('<tr>');
            const $name = $('<td>').attr('data-label', 'Nume').text(teacher.name);
            const $subject = $('<td>').attr('data-label', 'Materie').text(teacher.subject);
            const $exp = $('<td>').attr('data-label', 'Experiență (ani)').text(teacher.experience);
            $row.append($name, $subject, $exp);
            $tableBody.append($row);
        });
    }

    function updateControls(pagination) {
        currentPage = parseInt(pagination.page, 10) || currentPage;
        currentPageSize = parseInt(pagination.pageSize, 10) || currentPageSize;
        totalPages = parseInt(pagination.totalPages, 10) || totalPages;

        $pageInfo.text('Pagina ' + currentPage + ' din ' + totalPages + ' | ' + (pagination.totalRecords || 0) + ' înregistrări');
        $prevBtn.prop('disabled', currentPage <= 1);
        $nextBtn.prop('disabled', currentPage >= totalPages);
    }

    function showError() {
        $tableBody.empty();
        const $errorRow = $('<tr>');
        const $cell = $('<td>').attr('colspan', 3).addClass('text-center error').text('Eroare la încărcarea datelor.');
        $errorRow.append($cell);
        $tableBody.append($errorRow);
        $pageInfo.text('Nu s-au putut încărca datele.');
        $prevBtn.prop('disabled', true);
        $nextBtn.prop('disabled', true);
    }

    function loadTeachers(page, pageSize) {
        setLoading(true);
        console.log("DADADA")

        $.ajax({
            url: 'get_teachers_paginated.php',
            method: 'GET',
            data: { page: page, pageSize: pageSize },
            dataType: 'json',
            headers: { 'Accept': 'application/json' },
            success: function (data) {
                if (!data || data.error) {
                    showError();
                    return;
                }
                renderTeachers(data.teachers);
                if (data.pagination) {
                    updateControls(data.pagination);
                } else {
                    updateControls({ page: page, pageSize: pageSize, totalRecords: (data.teachers || []).length, totalPages: 1 });
                }
            },
            error: function (xhr, status, err) {
                console.error('AJAX error:', status, err);
                showError();
            },
            complete: function () {
                setLoading(false);
                $pageSizeInput.prop('disabled', false);
                $applyBtn.prop('disabled', false);
                $prevBtn.prop('disabled', currentPage <= 1);
                $nextBtn.prop('disabled', currentPage >= totalPages);
            }
        });
    }

    $applyBtn.on('click', function () {
        const newPageSize = Math.max(1, parseInt($pageSizeInput.val(), 10) || initialPageSize);
        $pageSizeInput.val(newPageSize);
        currentPage = 1;
        loadTeachers(currentPage, newPageSize);
    });

    $pageSizeInput.on('keydown', function (e) {
        if (e.key === 'Enter' || e.which === 13) {
            e.preventDefault();
            $applyBtn.click();
        }
    });

    $prevBtn.on('click', function () {
        if (currentPage > 1) {
            currentPage -= 1;
            loadTeachers(currentPage, currentPageSize);
        }
    });

    $nextBtn.on('click', function () {
        if (currentPage < totalPages) {
            currentPage += 1;
            loadTeachers(currentPage, currentPageSize);
        }
    });

    $themeToggle.on('click', function () {
        const isDark = $body.hasClass('dark-theme');
        const newTheme = isDark ? 'light' : 'dark';
        applyTheme(newTheme);
        localStorage.setItem('theme', newTheme);
    });

    loadTeachers(currentPage, currentPageSize);
});