$(document).ready(function () {
    const registerForm = $('#registerForm');
    const loginForm = $('#loginForm');

    const countyAndCityData = {
        "București": ["Sector 1", "Sector 2", "Sector 3", "Sector 4", "Sector 5", "Sector 6"],
        "Cluj": ["Cluj-Napoca", "Dej", "Gherla", "Turda", "Câmpia Turzii"],
        "Timiș": ["Timișoara", "Lugoj", "Sânnicolau Mare", "Jimbolia"],
        "Iași": ["Iași", "Pașcani", "Târgu Frumos"],
        "Constanța": ["Constanța", "Mangalia", "Medgidia", "Năvodari"]
    };

    const countySelect = $('#countySelect');
    const citySelect = $('#citySelect');

    if (countySelect.length && citySelect.length) {
        countySelect.html('<option value="">Alegeți un județ</option>');
        $.each(countyAndCityData, function (county) {
            countySelect.append($('<option>', {
                value: county,
                text: county
            }));
        });

        countySelect.change(function () {
            const selectedCounty = $(this).val();
            citySelect.html('').prop('disabled', true);

            if (selectedCounty) {
                const cities = countyAndCityData[selectedCounty];
                if (cities) {
                    citySelect.prop('disabled', false).html('<option value="">Alegeți un oraș</option>');
                    $.each(cities, function (index, city) {
                        citySelect.append($('<option>', {
                            value: city,
                            text: city
                        }));
                    });
                }
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
        const phone = $('#phoneInput');
        const classSelect = $('#classSelect');
        const sessionType = $('input[name="tip_sedinta"]:checked');
        const terms = $('#termsInput');

        function saveRegisterForm() {
            const formData = {
                lastName: lastName.val(),
                firstName: firstName.val(),
                email: email.val(),
                password: password.val(),
                age: $('#ageInput').val(),
                date: date.val(),
                phone: phone.val(),
                county: $('#countySelect').val(),
                city: $('#citySelect').val(),
                class: classSelect.val(),
                subject: $('select[name="materie"]').val(),
                interval: $('select[name="interval"]').val(),
                sessionType: $('input[name="tip_sedinta"]:checked').val(),
                budget: $('input[name="buget"]').val(),
                newsletter: $('input[name="regulament"]').is(':checked'),
                terms: terms.is(':checked'),
                observations: $('textarea[name="observatii"]').val()
            };
            localStorage.setItem('registerFormData', JSON.stringify(formData));
        }

        function loadRegisterForm() {
            const formData = JSON.parse(localStorage.getItem('registerFormData'));
            if (formData) {
                lastName.val(formData.lastName);
                firstName.val(formData.firstName);
                email.val(formData.email);
                password.val(formData.password);
                $('#ageInput').val(formData.age);
                date.val(formData.date);
                phone.val(formData.phone);
                
                if (formData.county) {
                    $('#countySelect').val(formData.county).change();
                    setTimeout(function() {
                        $('#citySelect').val(formData.city);
                    }, 100);
                }

                classSelect.val(formData.class);
                $('select[name="materie"]').val(formData.subject);
                $('select[name="interval"]').val(formData.interval);
                if (formData.sessionType) {
                    $('input[name="tip_sedinta"][value="' + formData.sessionType + '"]').prop('checked', true);
                }
                $('input[name="buget"]').val(formData.budget);
                $('input[name="regulament"]').prop('checked', formData.newsletter);
                terms.prop('checked', formData.terms);
                $('textarea[name="observatii"]').val(formData.observations);
            }
        }

        loadRegisterForm();

        registerForm.on('change', 'input, select, textarea', function() {
            saveRegisterForm();
        });

        registerForm.submit(function (event) {
            event.preventDefault();

            const inputs = [
                '#lastNameInput',
                '#firstNameInput',
                '#emailInput',
                '#passwordInput',
                '#dateInput',
                '#phoneInput',
                '#classSelect',
                '#termsInput'
            ];

            $(inputs.join(', ')).removeClass('input-error');
            $('input[name="tip_sedinta"]').parent().removeClass('input-error');
            $('#termsInput').parent().removeClass('input-error');

            const registerErrorText = $('#registerErrorText');
            registerErrorText.text('');

            let hasErrors = false;
            const errorMessages = [];

            if (lastName.val().trim() === '') {
                errorMessages.push('Numele este obligatoriu');
                lastName.addClass('input-error');
                hasErrors = true;
            }

            if (firstName.val().trim() === '') {
                errorMessages.push('Prenumele este obligatoriu');
                firstName.addClass('input-error');
                hasErrors = true;
            }

            if (email.val().trim() === '') {
                errorMessages.push('Emailul este obligatoriu');
                email.addClass('input-error');
                hasErrors = true;
            } else if (!/^\S+@\S+\.\S+$/.test(email.val())) {
                errorMessages.push('Email invalid');
                email.addClass('input-error');
                hasErrors = true;
            }

            if (password.val().trim() === '') {
                errorMessages.push('Parola este obligatorie');
                password.addClass('input-error');
                hasErrors = true;
            } else if (password.val().length < 6) {
                errorMessages.push('Parola trebuie sa aiba minim 6 caractere');
                password.addClass('input-error');
                hasErrors = true;
            }

            if (date.val().trim() === '') {
                errorMessages.push('Data nasterii este obligatorie');
                date.addClass('input-error');
                hasErrors = true;
            } else {
                const birthDate = new Date(date.val());
                const today = new Date();
                let age = today.getFullYear() - birthDate.getFullYear();
                const m = today.getMonth() - birthDate.getMonth();
                if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }
                if (age < 7) {
                    errorMessages.push('Trebuie sa ai minim 7 ani');
                    date.addClass('input-error');
                    hasErrors = true;
                }
            }

            if (phone.val().trim() === '') {
                errorMessages.push('Telefonul este obligatoriu');
                phone.addClass('input-error');
                hasErrors = true;
            } else if (!/^07\d{8}$/.test(phone.val())) {
                errorMessages.push('Numar de telefon invalid');
                phone.addClass('input-error');
                hasErrors = true;
            }

            if (!classSelect.val() || classSelect.val().length === 0) {
                errorMessages.push('Selecteaza cel putin o clasa');
                classSelect.addClass('input-error');
                hasErrors = true;
            }

            if (sessionType.length === 0) {
                errorMessages.push('Selecteaza tipul sedintei');
                $('input[name="tip_sedinta"]').parent().addClass('input-error');
                hasErrors = true;
            }

            if (!terms.is(':checked')) {
                errorMessages.push('Trebuie sa fii de acord cu prelucrarea datelor');
                terms.parent().addClass('input-error');
                hasErrors = true;
            }

            if (hasErrors) {
                registerErrorText.html(errorMessages.join('<br>'));
            } else {
                console.log('Formularul a fost trimis cu succes!');
                localStorage.removeItem('registerFormData');
            }
        });

        registerForm.on('reset', function () {
            $('#registerErrorText').html('');
            $('.input-error').removeClass('input-error');
            localStorage.removeItem('registerFormData');
            $('#citySelect').html('<option value="">Alegeți un județ mai întâi</option>').prop('disabled', true);
        });


    }

    if (loginForm.length) {
        loginForm.submit(function (event) {
            event.preventDefault();

            const inputs = ['#emailInput', '#passwordInput'];
            $(inputs.join(', ')).removeClass('input-error');

            const registerErrorText = $('#registerErrorText');
            registerErrorText.text('');

            let hasErrors = false;
            const errorMessages = [];

            const email = $('#emailInput');
            const password = $('#passwordInput');

            if (email.val().trim() === '') {
                errorMessages.push('Emailul este obligatoriu');
                email.addClass('input-error');
                hasErrors = true;
            } else if (!/^\S+@\S+\.\S+$/.test(email.val())) {
                errorMessages.push('Email invalid');
                email.addClass('input-error');
                hasErrors = true;
            }

            if (password.val().trim() === '') {
                errorMessages.push('Parola este obligatorie');
                password.addClass('input-error');
                hasErrors = true;
            } else if (password.val().length < 6) {
                errorMessages.push('Parola trebuie sa aiba minim 6 caractere');
                password.addClass('input-error');
                hasErrors = true;
            }

            if (hasErrors) {
                registerErrorText.html(errorMessages.join('<br>'));
            } else {
                console.log('Formularul a fost trimis cu succes!');
            }
        });
    }

    const ageInput = $('#ageInput');
    const dateInput = $('#dateInput');

    if (ageInput.length && dateInput.length) {
        ageInput.on('input', function () {
            const age = parseInt($(this).val(), 10);
            if (!isNaN(age) && age >= 7) {
                const currentYear = new Date().getFullYear();
                const birthYear = currentYear - age;
                dateInput.val(`${birthYear}-01-01`);
            }
        });

        dateInput.change(function () {
            const birthDate = new Date($(this).val());
            if (!isNaN(birthDate.getTime())) {
                const today = new Date();
                let age = today.getFullYear() - birthDate.getFullYear();
                const m = today.getMonth() - birthDate.getMonth();
                if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }
                ageInput.val(age);
            }
        });
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

    let teachers = [
        { name: 'Popescu Ion', subject: 'Matematică', experience: 10 },
        { name: 'Ionescu Ana', subject: 'Informatică', experience: 5 },
        { name: 'Georgescu Dan', subject: 'Fizică', experience: 15 },
        { name: 'Marinescu Elena', subject: 'Chimie', experience: 8 },
    ];

    let sortColumn = null;
    let sortDirection = 'asc';

    function renderTable() {
        const tableBody = $('#teachers-table tbody');
        if (!tableBody.length) return;

        tableBody.html('');

        $.each(teachers, function (index, teacher) {
            const row = $('<tr>').html(`
                <td>${teacher.name}</td>
                <td>${teacher.subject}</td>
                <td>${teacher.experience}</td>
            `);
            tableBody.append(row);
        });
    }

    function sortTable(column) {
        if (sortColumn === column) {
            sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            sortColumn = column;
            sortDirection = 'asc';
        }

        teachers.sort((a, b) => {
            const aValue = a[column];
            const bValue = b[column];

            if (aValue < bValue) {
                return sortDirection === 'asc' ? -1 : 1;
            }
            if (aValue > bValue) {
                return sortDirection === 'asc' ? 1 : -1;
            }
            return 0;
        });

        updateSortIndicators();
        renderTable();
    }

    function updateSortIndicators() {
        $('#teachers-table th').removeClass('asc desc').each(function () {
            if ($(this).data('column') === sortColumn) {
                $(this).addClass(sortDirection);
            }
        });
    }

    $('#teachers-table th').click(function () {
        const column = $(this).data('column');
        sortTable(column);
    });

    renderTable();

    let verticalSortKey = 'name';
    let verticalSortDirection = 'asc';

    const renderVerticalTable = () => {
        const table = $('#vertical-teachers-table');
        if (!table.length) return;

        table.html('');

        const headers = {
            name: 'Nume',
            subject: 'Materie',
            experience: 'Experiență (ani)',
        };

        $.each(headers, function (key, headerText) {
            const row = $('<tr>');
            const headerCell = $('<th>').text(headerText).data('column', key).html(headerText + '<span class="sort-indicator"></span>');
            row.append(headerCell);

            $.each(teachers, function (index, teacher) {
                const cell = $('<td>').text(teacher[key]);
                row.append(cell);
            });

            table.append(row);
        });

        addVerticalSortEventListeners();
        updateVerticalSortIndicators();
    };

    const sortVerticalData = (key) => {
        if (verticalSortKey === key) {
            verticalSortDirection = verticalSortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            verticalSortKey = key;
            verticalSortDirection = 'asc';
        }

        teachers.sort((a, b) => {
            const valA = a[verticalSortKey];
            const valB = b[verticalSortKey];

            if (typeof valA === 'string') {
                return verticalSortDirection === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA);
            } else {
                return verticalSortDirection === 'asc' ? valA - valB : valB - valA;
            }
        });

        renderVerticalTable();
    };

    const addVerticalSortEventListeners = () => {
        $('#vertical-teachers-table th').click(function () {
            sortVerticalData($(this).data('column'));
        });
    };

    const updateVerticalSortIndicators = () => {
        $('#vertical-teachers-table th').removeClass('asc desc').each(function () {
            if ($(this).data('column') === verticalSortKey) {
                $(this).addClass(verticalSortDirection);
            }
        });
    };

    renderVerticalTable();

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

