$(document).ready(function () {
    const registerForm = $('#registerForm');
    const loginForm = $('#loginForm');

    const countySelect = $('#countySelect');
    const citySelect = $('#citySelect');

    if (countySelect.length && citySelect.length) {
        countySelect.change(function () {
            const selectedCountyId = $(this).val();
            citySelect.html('').prop('disabled', true);

            if (selectedCountyId) {
                $(this).find('option[value=""]').remove();
                
                $.getJSON('get_cities.php', { county_id: selectedCountyId }, function (cities) {
                    if (cities.length > 0) {
                        citySelect.prop('disabled', false).html('<option value="">Alegeți un oraș</option>');
                        $.each(cities, function (index, city) {
                            citySelect.append($('<option>', {
                                value: city.id,
                                text: city.name
                            }));
                        });
                    } else {
                        citySelect.html('<option value="">Nu există orașe pentru acest județ</option>');
                    }
                });
            } else {
                citySelect.html('<option value="">Alegeți un județ mai întâi</option>');
            }
        });
    }

    if (registerForm.length) {
        const lastName = $('#lastNameInput');
        const firstName = $('#firstNameInput');
        const email = $('#emailInput');
        const password = $('#passwordInput');
        const date = $('#dateInput');
        const classSelect = $('#classSelect');
        const terms = $('#termsInput');

        function saveRegisterForm() {
            const formData = {
                lastName: lastName.val(),
                firstName: firstName.val(),
                email: email.val(),
                date: date.val(),
                county: $('#countySelect').val(),
                city: $('#citySelect').val(),
                class: classSelect.val(),
                newsletter: $('input[name="regulament"]').is(':checked'),
                terms: terms.is(':checked'),
            };
            localStorage.setItem('registerFormData', JSON.stringify(formData));
        }

        function loadRegisterForm() {
            if ($('.success-container').length > 0) {
                clearSavedRegisterData();
                return;
            }
            const formData = JSON.parse(localStorage.getItem('registerFormData'));
            if (formData) {
                lastName.val(formData.lastName);
                firstName.val(formData.firstName);
                email.val(formData.email);
                date.val(formData.date);
                
                if (formData.county) {
                    $('#countySelect').val(formData.county).change();
                    setTimeout(function() {
                        $('#citySelect').val(formData.city);
                    }, 100);
                }

                classSelect.val(formData.class);
                $('input[name="regulament"]').prop('checked', formData.newsletter);
                terms.prop('checked', formData.terms);
            }
        }

        loadRegisterForm();

        registerForm.on('change', 'input, select, textarea', function() {
            saveRegisterForm();
        });

        registerForm.on('reset', function () {
            clearSavedRegisterData();
        });

        function clearSavedRegisterData() {
            localStorage.removeItem('registerFormData');
            $('#citySelect').html('<option value="">Alegeți un județ mai întâi</option>').prop('disabled', true);
        }
    }

    const carouselData = [
        { link: '#', text: 'Experienta 8 ani', image: 'https://placehold.co/800x400/3498db/ffffff/png?text=Matematica' },
        { link: '#', text: 'Experienta 5 ani', image: 'https://placehold.co/800x400/2ecc71/ffffff/png?text=Informatica' },
        { link: '#', text: 'Experienta 10 ani', image: 'https://placehold.co/800x400/e74c3c/ffffff/png?text=Fizica' },
        { link: '#', text: 'Experienta 7 ani', image: 'https://placehold.co/800x400/f1c40f/ffffff/png?text=Chimie' }
    ];

    const carouselContainer = $('.carousel-container');
    if (carouselContainer.length) {
        const imageElement = carouselContainer.find('.carousel-image');
        const textElement = carouselContainer.find('.carousel-text');
        const linkElement = carouselContainer.find('.carousel-link');
        const prevButton = carouselContainer.find('.prev');
        const nextButton = carouselContainer.find('.next');

        let currentIndex = 0;
        let intervalId = null;

        function updateSlide(index) {
            const slide = carouselData[index];
            imageElement.attr('src', slide.image).attr('alt', slide.text);
            textElement.text(slide.text);
            linkElement.attr('href', slide.link);
        }

        function nextSlide() {
            currentIndex = (currentIndex + 1) % carouselData.length;
            updateSlide(currentIndex);
        }

        function prevSlide() {
            currentIndex = (currentIndex - 1 + carouselData.length) % carouselData.length;
            updateSlide(currentIndex);
        }

        function startCarousel() {
            stopCarousel();
            intervalId = setInterval(nextSlide, 3000);
        }

        function stopCarousel() {
            clearInterval(intervalId);
        }

        nextButton.click(function () {
            nextSlide();
            startCarousel();
        });

        prevButton.click(function () {
            prevSlide();
            startCarousel();
        });

        updateSlide(currentIndex);
        startCarousel();
    }

    let sortColumn = 'name';
    let sortDirection = 'asc';

    function renderTable(teachers) {
        const tableBody = $('#teachers-table tbody');
        if (!tableBody.length) return;

        tableBody.html('');

        if (!teachers || teachers.length === 0) {
            const numColumns = $('#teachers-table th').length;
            tableBody.html(`<tr><td colspan="${numColumns}" style="text-align:center;">Nu sunt profesori de afișat.</td></tr>`);
            return;
        }

        $.each(teachers, function (index, teacher) {
            const row = $('<tr>').html(`
                <td>${teacher.name}</td>
                <td>${teacher.subject}</td>
                <td>${teacher.experience}</td>
            `);
            tableBody.append(row);
        });
    }

    function loadTeachers(sortBy, sortDir) {
        $.ajax({
            url: 'get_teachers.php',
            method: 'GET',
            data: {
                sortBy: sortBy,
                sortDir: sortDir
            },
            dataType: 'json',
            success: function(data) {
                renderTable(data);
                updateSortIndicators();
            },
            error: function(xhr, status, error) {
                console.error("Eroare la încărcarea profesorilor:", error);
                const tableBody = $('#teachers-table tbody');
                if (tableBody.length) {
                    const numColumns = $('#teachers-table th').length;
                    tableBody.html(`<tr><td colspan="${numColumns}" style="text-align:center;">Eroare la încărcarea datelor.</td></tr>`);
                }
            }
        });
    }

    function updateSortIndicators() {
        $('#teachers-table th').removeClass('asc desc').each(function () {
            const column = $(this).data('column');
            if (column === sortColumn) {
                $(this).addClass(sortDirection);
            }
        });
    }

    $('#teachers-table th').click(function () {
        const column = $(this).data('column');
        if (sortColumn === column) {
            sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            sortColumn = column;
            sortDirection = 'asc';
        }
        loadTeachers(sortColumn, sortDirection);
    });

    if ($('#teachers-table').length) {
        loadTeachers(sortColumn, sortDirection);
    }

    $('.collapsible-list .expandable > :first-child').click(function () {
        $(this).parent().toggleClass('expanded');
    });

    const themeToggle = $('#theme-toggle');
    const body = $('body');

    const applyTheme = (theme) => {
        if (theme === 'dark') {
            body.addClass('dark-theme');
        } else {
            body.removeClass('dark-theme');
        }
    };

    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        applyTheme(savedTheme);
    }

    if (themeToggle.length) {
        themeToggle.click(function () {
            const isDark = body.hasClass('dark-theme');
            const newTheme = isDark ? 'light' : 'dark';
            applyTheme(newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }
});

