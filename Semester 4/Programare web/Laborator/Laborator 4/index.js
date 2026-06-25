const registerForm = document.querySelector('#registerForm');
const loginForm = document.querySelector('#loginForm')

const countyAndCityData = {
    "București": ["Sector 1", "Sector 2", "Sector 3", "Sector 4", "Sector 5", "Sector 6"],
    "Cluj": ["Cluj-Napoca", "Dej", "Gherla", "Turda", "Câmpia Turzii"],
    "Timiș": ["Timișoara", "Lugoj", "Sânnicolau Mare", "Jimbolia"],
    "Iași": ["Iași", "Pașcani", "Târgu Frumos"],
    "Constanța": ["Constanța", "Mangalia", "Medgidia", "Năvodari"]
};

const countySelect = document.querySelector('#countySelect');
const citySelect = document.querySelector('#citySelect');

if (countySelect && citySelect) {
    const counties = Object.keys(countyAndCityData);
    countySelect.innerHTML = '<option value="">Alegeți un județ</option>'; 
    counties.forEach(county => {
        const option = document.createElement('option');
        option.value = county;
        option.textContent = county;
        countySelect.appendChild(option);
    });

    countySelect.addEventListener('change', () => {
        const selectedCounty = countySelect.value;
        citySelect.innerHTML = ''; 
        citySelect.disabled = true;

        if (selectedCounty) {
            const cities = countyAndCityData[selectedCounty];
            if (cities) {
                citySelect.disabled = false;
                citySelect.innerHTML = '<option value="">Alegeți un oraș</option>'; 
                cities.forEach(city => {
                    const option = document.createElement('option');
                    option.value = city;
                    option.textContent = city;
                    citySelect.appendChild(option);
                });
            }
        } else {
            citySelect.innerHTML = '<option value="">Alegeți un județ mai întâi</option>';
        }
    });
}

if (registerForm) {
    registerForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const inputs = [
            'lastNameInput',
            'firstNameInput',
            'emailInput',
            'passwordInput',
            'dateInput',
            'phoneInput',
            'classSelect',
            'termsInput'
        ];

        inputs.forEach(id => {
            const input = document.querySelector(`#${id}`);
            if (input) {
                input.classList.remove('input-error');
            }
        });
        document.querySelector('input[name="tip_sedinta"]').parentElement.classList.remove('input-error');
        document.querySelector('#termsInput').parentElement.classList.remove('input-error');

        const lastName = document.querySelector('#lastNameInput');
        const firstName = document.querySelector('#firstNameInput');
        const email = document.querySelector('#emailInput');
        const password = document.querySelector('#passwordInput');
        const date = document.querySelector('#dateInput');
        const phone = document.querySelector('#phoneInput');
        const classSelect = document.querySelector('#classSelect');
        const sessionType = document.querySelector('input[name="tip_sedinta"]:checked');
        const terms = document.querySelector('#termsInput');

        const registerErrorText = document.querySelector('#registerErrorText');

        registerErrorText.textContent = '';

        let hasErrors = false;
        const errorMessages = [];

        if (lastName.value.trim() === '') {
            errorMessages.push('Numele este obligatoriu');
            lastName.classList.add('input-error');
            hasErrors = true;
        }

        if (firstName.value.trim() === '') {
            errorMessages.push('Prenumele este obligatoriu');
            firstName.classList.add('input-error');
            hasErrors = true;
        }

        if (email.value.trim() === '') {
            errorMessages.push('Emailul este obligatoriu');
            email.classList.add('input-error');
            hasErrors = true;
        } else if (!/^\S+@\S+\.\S+$/.test(email.value)) {
            errorMessages.push('Email invalid');
            email.classList.add('input-error');
            hasErrors = true;
        }

        if (password.value.trim() === '') {
            errorMessages.push('Parola este obligatorie');
            password.classList.add('input-error');
            hasErrors = true;
        } else if (password.value.length < 6) {
            errorMessages.push('Parola trebuie sa aiba minim 6 caractere');
            password.classList.add('input-error');
            hasErrors = true;
        }

        if (date.value.trim() === '') {
            errorMessages.push('Data nasterii este obligatorie');
            date.classList.add('input-error');
            hasErrors = true;
        } else {
            const birthDate = new Date(date.value);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            if (age < 7) {
                errorMessages.push('Trebuie sa ai minim 7 ani');
                date.classList.add('input-error');
                hasErrors = true;
            }
        }

        if (phone.value.trim() === '') {
            errorMessages.push('Telefonul este obligatoriu');
            phone.classList.add('input-error');
            hasErrors = true;
        } else if (!/^07\d{8}$/.test(phone.value)) {
            errorMessages.push('Numar de telefon invalid');
            phone.classList.add('input-error');
            hasErrors = true;
        }

        if (classSelect.selectedOptions.length === 0) {
            errorMessages.push('Selecteaza cel putin o clasa');
            classSelect.classList.add('input-error');
            hasErrors = true;
        }

        if (!sessionType) {
            errorMessages.push('Selecteaza tipul sedintei');
            document.querySelector('input[name="tip_sedinta"]').parentElement.classList.add('input-error');
            hasErrors = true;
        }

        if (!terms.checked) {
            errorMessages.push('Trebuie sa fii de acord cu prelucrarea datelor');
            terms.parentElement.classList.add('input-error');
            hasErrors = true;
        }

        if (hasErrors) {
            registerErrorText.innerHTML = errorMessages.join('<br>');
        } else {
            console.log('Formularul a fost trimis cu succes!');
        }
    });

    registerForm.addEventListener('reset', () => {
        const registerErrorText = document.querySelector('#registerErrorText');

        const inputs = [
            'lastNameInput',
            'firstNameInput',
            'emailInput',
            'passwordInput',
            'dateInput',
            'phoneInput',
            'classSelect',
            'termsInput'
        ];

        inputs.forEach(id => {
            const input = document.querySelector(`#${id}`);
            if (input) {
                input.classList.remove('input-error');
            }
        });
        document.querySelector('input[name="tip_sedinta"]').parentElement.classList.remove('input-error');
        document.querySelector('#termsInput').parentElement.classList.remove('input-error');

        registerErrorText.innerHTML = '';
    });
}

if(loginForm) {
    loginForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const inputs = [
            'emailInput',
            'passwordInput',
        ];

        inputs.forEach(id => {
            const input = document.querySelector(`#${id}`);
            if (input) {
                input.classList.remove('input-error');
            }
        });

        const registerErrorText = document.querySelector('#registerErrorText');

        registerErrorText.textContent = '';

        let hasErrors = false;
        const errorMessages = [];

        const email = document.querySelector('#emailInput');
        const password = document.querySelector('#passwordInput');

        if (email.value.trim() === '') {
            errorMessages.push('Emailul este obligatoriu');
            email.classList.add('input-error');
            hasErrors = true;
        } else if (!/^\S+@\S+\.\S+$/.test(email.value)) {
            errorMessages.push('Email invalid');
            email.classList.add('input-error');
            hasErrors = true;
        }

        if (password.value.trim() === '') {
            errorMessages.push('Parola este obligatorie');
            password.classList.add('input-error');
            hasErrors = true;
        } else if (password.value.length < 6) {
            errorMessages.push('Parola trebuie sa aiba minim 6 caractere');
            password.classList.add('input-error');
            hasErrors = true;
        }

        if (hasErrors) {
            registerErrorText.innerHTML = errorMessages.join('<br>');
        } else {
            console.log('Formularul a fost trimis cu succes!');
        }
    });

    loginForm.addEventListener('reset', () => {
        
    });
}

const ageInput = document.querySelector('#ageInput');
const dateInput = document.querySelector('#dateInput');

if (ageInput && dateInput) {
    ageInput.addEventListener('input', () => {
        const age = parseInt(ageInput.value, 10);
        if (!isNaN(age) && age >= 7) {
            const currentYear = new Date().getFullYear();
            const birthYear = currentYear - age;
            dateInput.value = `${birthYear}-01-01`;
        }
    });

    dateInput.addEventListener('change', () => {
        const birthDate = new Date(dateInput.value);
        if (!isNaN(birthDate.getTime())) {
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            ageInput.value = age;
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const carouselData = [
        {
            link: '#',
            text: 'Experienta 8 ani',
            image: 'https://placehold.co/800x400/3498db/ffffff/png?text=Matematica'
        },
        {
            link: '#',
            text: 'Experienta 5 ani',
            image: 'https://placehold.co/800x400/2ecc71/ffffff/png?text=Informatica'
        },
        {
            link: '#',
            text: 'Experienta 10 ani',
            image: 'https://placehold.co/800x400/e74c3c/ffffff/png?text=Fizica'
        },
        {
            link: '#',
            text: 'Experienta 7 ani',
            image: 'https://placehold.co/800x400/f1c40f/ffffff/png?text=Chimie'
        }
    ];

    const carouselContainer = document.querySelector('.carousel-container');
    if (carouselContainer) {
        const imageElement = carouselContainer.querySelector('.carousel-image');
        const textElement = carouselContainer.querySelector('.carousel-text');
        const linkElement = carouselContainer.querySelector('.carousel-link');
        const prevButton = carouselContainer.querySelector('.prev');
        const nextButton = carouselContainer.querySelector('.next');

        let currentIndex = 0;
        let intervalId = null;

        function updateSlide(index) {
            const slide = carouselData[index];
            imageElement.src = slide.image;
            imageElement.alt = slide.text;
            textElement.textContent = slide.text;
            linkElement.href = slide.link;
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

        nextButton.addEventListener('click', () => {
            nextSlide();
            startCarousel(); 
        });

        prevButton.addEventListener('click', () => {
            prevSlide();
            startCarousel(); 
        });

        updateSlide(currentIndex);
        startCarousel();
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const teachers = [
        { name: 'Popescu Ion', subject: 'Matematică', experience: 10 },
        { name: 'Ionescu Ana', subject: 'Informatică', experience: 5 },
        { name: 'Georgescu Dan', subject: 'Fizică', experience: 15 },
        { name: 'Marinescu Elena', subject: 'Chimie', experience: 8 },
    ];

    let sortColumn = null;
    let sortDirection = 'asc';

    function renderTable() {
        const tableBody = document.querySelector('#teachers-table tbody');
        if (!tableBody) return;

        tableBody.innerHTML = '';

        teachers.forEach(teacher => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${teacher.name}</td>
                <td>${teacher.subject}</td>
                <td>${teacher.experience}</td>
            `;
            tableBody.appendChild(row);
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
        document.querySelectorAll('#teachers-table th').forEach(th => {
            th.classList.remove('asc', 'desc');
            if (th.dataset.column === sortColumn) {
                th.classList.add(sortDirection);
            }
        });
    }

    document.querySelectorAll('#teachers-table th').forEach(th => {
        th.addEventListener('click', () => {
            const column = th.dataset.column;
            sortTable(column);
        });
    });

    renderTable();
});

document.addEventListener('DOMContentLoaded', () => {
    let teachers = [
        { name: 'Popescu Ion', subject: 'Matematică', experience: 10 },
        { name: 'Ionescu Ana', subject: 'Informatică', experience: 5 },
        { name: 'Georgescu Dan', subject: 'Fizică', experience: 15 },
        { name: 'Marinescu Elena', subject: 'Chimie', experience: 8 },
    ];

    let sortKey = 'name';
    let sortDirection = 'asc';

    const renderVerticalTable = () => {
        const table = document.querySelector('#vertical-teachers-table');
        if (!table) return;

        table.innerHTML = '';

        const headers = {
            name: 'Nume',
            subject: 'Materie',
            experience: 'Experiență (ani)',
        };

        Object.keys(headers).forEach(key => {
            const row = document.createElement('tr');
            const headerCell = document.createElement('th');
            headerCell.textContent = headers[key];
            headerCell.dataset.column = key;
            headerCell.innerHTML += '<span class="sort-indicator"></span>';
            row.appendChild(headerCell);

            teachers.forEach(teacher => {
                const cell = document.createElement('td');
                cell.textContent = teacher[key];
                row.appendChild(cell);
            });

            table.appendChild(row);
        });

        addSortEventListeners();
        updateSortIndicators();
    };

    const sortData = (key) => {
        if (sortKey === key) {
            sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            sortKey = key;
            sortDirection = 'asc';
        }

        teachers.sort((a, b) => {
            const valA = a[sortKey];
            const valB = b[sortKey];

            if (typeof valA === 'string') {
                return sortDirection === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA);
            } else {
                return sortDirection === 'asc' ? valA - valB : valB - valA;
            }
        });

        renderVerticalTable();
    };

    const addSortEventListeners = () => {
        document.querySelectorAll('.vertical-table th').forEach(th => {
            th.addEventListener('click', () => {
                sortData(th.dataset.column);
            });
        });
    };

    const updateSortIndicators = () => {
        document.querySelectorAll('.vertical-table th').forEach(th => {
            th.classList.remove('asc', 'desc');
            if (th.dataset.column === sortKey) {
                th.classList.add(sortDirection);
            }
        });
    };

    renderVerticalTable();
});

document.addEventListener('DOMContentLoaded', () => {
    const collapsibleLists = document.querySelectorAll('.collapsible-list'); 
    collapsibleLists.forEach(list => {
        const items = list.querySelectorAll('.expandable');
        items.forEach(item => {
            const header = item.firstElementChild;
            const childList = item.querySelector(':scope > ul');
            if (header && childList) {
                header.addEventListener('click', (event) => {
                    item.classList.toggle('expanded');
                });
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const themeToggle = document.getElementById('theme-toggle');
    const body = document.body;

    const applyTheme = (theme) => {
        if (theme === 'dark') {
            body.classList.add('dark-theme');
        } else {
            body.classList.remove('dark-theme');
        }
    };

    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        applyTheme(savedTheme);
    }

    if (themeToggle) {
        themeToggle.addEventListener('click', () => {
            const isDark = body.classList.contains('dark-theme');
            const newTheme = isDark ? 'light' : 'dark';
            applyTheme(newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }
});

