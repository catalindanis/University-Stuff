document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.querySelector('#teachers-table tbody');
    const pageSizeInput = document.getElementById('page-size');
    const applyPageSizeButton = document.getElementById('apply-page-size');
    const previousButton = document.getElementById('previous-page');
    const nextButton = document.getElementById('next-page');
    const pageInfo = document.getElementById('page-info');
    const table = document.getElementById('teachers-table');
    const themeToggle = document.getElementById('theme-toggle');
    const body = document.body;

    if (!tableBody || !pageSizeInput || !applyPageSizeButton || !previousButton || !nextButton || !pageInfo || !table) {
        return;
    }

    const initialPageSize = Math.max(1, parseInt(pageSizeInput.value, 10) || 5);
    let currentPage = 1;
    let currentPageSize = initialPageSize;
    let totalPages = 1;

    function applyTheme(theme) {
        if (theme === 'dark') {
            body.classList.add('dark-theme');
        } else {
            body.classList.remove('dark-theme');
        }
    }

    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        applyTheme(savedTheme);
    }

    function setLoadingState(isLoading) {
        previousButton.disabled = isLoading;
        nextButton.disabled = isLoading;
        applyPageSizeButton.disabled = isLoading;
        pageSizeInput.disabled = isLoading;
    }

    function renderTeachers(teachers) {
        tableBody.innerHTML = '';

        if (!teachers || teachers.length === 0) {
            const emptyRow = document.createElement('tr');
            const cell = document.createElement('td');
            cell.colSpan = 3;
            cell.className = 'text-center';
            cell.textContent = 'Nu sunt profesori de afișat.';
            emptyRow.appendChild(cell);
            tableBody.appendChild(emptyRow);
            return;
        }

        teachers.forEach(function (teacher) {
            const row = document.createElement('tr');

            const nameCell = document.createElement('td');
            nameCell.setAttribute('data-label', 'Nume');
            nameCell.textContent = teacher.name;

            const subjectCell = document.createElement('td');
            subjectCell.setAttribute('data-label', 'Materie');
            subjectCell.textContent = teacher.subject;

            const experienceCell = document.createElement('td');
            experienceCell.setAttribute('data-label', 'Experiență (ani)');
            experienceCell.textContent = teacher.experience;

            row.appendChild(nameCell);
            row.appendChild(subjectCell);
            row.appendChild(experienceCell);
            tableBody.appendChild(row);
        });
    }

    function updateControls(pagination) {
        currentPage = pagination.page;
        currentPageSize = pagination.pageSize;
        totalPages = pagination.totalPages;

        pageInfo.textContent = 'Pagina ' + currentPage + ' din ' + totalPages + ' | ' + pagination.totalRecords + ' înregistrări';
        previousButton.disabled = currentPage <= 1;
        nextButton.disabled = currentPage >= totalPages;
    }

    async function loadTeachers(page, pageSize) {
        setLoadingState(true);

        try {
            const response = await fetch('get_teachers_paginated_xml.php?page=' + encodeURIComponent(page) + '&pageSize=' + encodeURIComponent(pageSize), {
                headers: {
                    'Accept': 'application/xml'
                }
            });

            const text = await response.text();
            const parser = new DOMParser();
            const xml = parser.parseFromString(text, 'application/xml');

            // check for XML parse errors
            if (xml.getElementsByTagName('parsererror').length > 0) {
                throw new Error('Invalid XML response from server.');
            }

            if (!response.ok) {
                const errNode = xml.getElementsByTagName('error')[0];
                throw new Error(errNode ? errNode.textContent : 'Eroare la server');
            }

            const teacherNodes = xml.getElementsByTagName('teacher');
            const teachers = [];
            for (let i = 0; i < teacherNodes.length; i++) {
                const t = teacherNodes[i];
                const name = t.getElementsByTagName('name')[0]?.textContent || '';
                const subject = t.getElementsByTagName('subject')[0]?.textContent || '';
                const experience = t.getElementsByTagName('experience')[0]?.textContent || '';
                teachers.push({ name, subject, experience });
            }

            const paginationNode = xml.getElementsByTagName('pagination')[0];
            const pagination = {
                page: page,
                pageSize: pageSize,
                totalRecords: teachers.length,
                totalPages: 1
            };

            if (paginationNode) {
                pagination.page = parseInt(paginationNode.getElementsByTagName('page')[0]?.textContent || page, 10);
                pagination.pageSize = parseInt(paginationNode.getElementsByTagName('pageSize')[0]?.textContent || pageSize, 10);
                pagination.totalRecords = parseInt(paginationNode.getElementsByTagName('totalRecords')[0]?.textContent || teachers.length, 10);
                pagination.totalPages = parseInt(paginationNode.getElementsByTagName('totalPages')[0]?.textContent || 1, 10);
            }

            renderTeachers(teachers);
            updateControls(pagination);
        } catch (error) {
            console.error('Eroare la încărcarea profesorilor:', error);
            tableBody.innerHTML = '';

            const errorRow = document.createElement('tr');
            const cell = document.createElement('td');
            cell.colSpan = 3;
            cell.className = 'text-center error';
            cell.textContent = 'Eroare la încărcarea datelor.';
            errorRow.appendChild(cell);
            tableBody.appendChild(errorRow);

            pageInfo.textContent = 'Nu s-au putut încărca datele.';
            previousButton.disabled = true;
            nextButton.disabled = true;
        } finally {
            setLoadingState(false);
            pageSizeInput.disabled = false;
            applyPageSizeButton.disabled = false;
            previousButton.disabled = currentPage <= 1;
            nextButton.disabled = currentPage >= totalPages;
        }
    }

    applyPageSizeButton.addEventListener('click', function () {
        const newPageSize = Math.max(1, parseInt(pageSizeInput.value, 10) || initialPageSize);
        pageSizeInput.value = newPageSize;
        currentPage = 1;
        loadTeachers(currentPage, newPageSize);
    });

    pageSizeInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            applyPageSizeButton.click();
        }
    });

    previousButton.addEventListener('click', function () {
        if (currentPage > 1) {
            currentPage -= 1;
            loadTeachers(currentPage, currentPageSize);
        }
    });

    nextButton.addEventListener('click', function () {
        if (currentPage < totalPages) {
            currentPage += 1;
            loadTeachers(currentPage, currentPageSize);
        }
    });

    if (themeToggle) {
        themeToggle.addEventListener('click', function () {
            const isDark = body.classList.contains('dark-theme');
            const newTheme = isDark ? 'light' : 'dark';
            applyTheme(newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }

    loadTeachers(currentPage, currentPageSize);
});
