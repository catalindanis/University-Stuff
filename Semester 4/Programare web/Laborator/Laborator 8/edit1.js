document.addEventListener("DOMContentLoaded", function() {
    const teacherSelect = document.getElementById('teacherSelect');

    const teacherNameInput = document.getElementById('teacherName');
    const teacherSubjectInput = document.getElementById('teacherSubject');
    const teacherExperienceInput = document.getElementById('teacherExperience');
    const updateTeacherButton = document.getElementById('updateTeacherButton');

    let teachersData = [];

    function loadTeachers() {
        fetch('get_teachers_edit.php')
            .then(response => response.json())
            .then(data => {
                teachersData = data.teachers;
                renderTeacherOptions();
            })
            .catch(error => {
                console.error('Error fetching teachers:', error);
            });
    }

    function renderTeacherOptions() {
        teacherSelect.innerHTML = '<option value="">Alegeți un profesor</option>';
        teachersData.forEach(teacher => {
            const option = document.createElement('option');
            option.value = teacher.id;
            option.textContent = teacher.id;
            teacherSelect.appendChild(option);
        });
        
        selectedTeacher = null;
        updateTeacherForm();
    }

    function updateTeacherForm() {
        teacherNameInput.value = selectedTeacher?.name ?? '';
        teacherSubjectInput.value = selectedTeacher?.subject ?? '';
        teacherExperienceInput.value = selectedTeacher?.experience ?? '';

        teacherNameInput.disabled = selectedTeacher === null;
        teacherSubjectInput.disabled = selectedTeacher === null;
        teacherExperienceInput.disabled = selectedTeacher === null;
        updateTeacherButton.disabled = true;

        teacherNameInput.classList.toggle('disabled', selectedTeacher === null);
        teacherSubjectInput.classList.toggle('disabled', selectedTeacher === null);
        teacherExperienceInput.classList.toggle('disabled', selectedTeacher === null);
        updateTeacherButton.classList.toggle('disabled', true);
    }

    let selectedTeacher = null;
    teacherSelect.addEventListener('change', function() {
        if (updateTeacherButton.disabled === false) {
            const confirmSwitch = confirm('Nu ai salvat modificarile facute. Esti sigur ca vrei sa schimbi profesorul selectat?');
            if (!confirmSwitch) {
                this.value = selectedTeacher ? selectedTeacher.id : '';
                return;
            }
        }   
        const selectedTeacherId = this.value;
        
        fetch(`get_teacher_by_id_edit.php?teacher_id=${selectedTeacherId}`)
            .then(response => response.json())
            .then(data => {
                if(data.teacher)
                    selectedTeacher = data.teacher;
                else
                    selectedTeacher = null;
                updateTeacherForm();
            })
            .catch(error => {
                console.error('Error fetching teacher details:', error);
            });
    });

    updateTeacherButton.addEventListener('click', function() {
        if (!selectedTeacher) {
            alert('Vă rugăm să selectați un profesor pentru a actualiza informațiile.');
            return;
        }

        const teacherNameInput = document.getElementById('teacherName');
        const teacherSubjectInput = document.getElementById('teacherSubject');
        const teacherExperienceInput = document.getElementById('teacherExperience');

        const updatedTeacher = {
            id: selectedTeacher.id,
            name: teacherNameInput.value,
            subject: teacherSubjectInput.value,
            experience: parseInt(teacherExperienceInput.value)
        };

        const body = JSON.stringify(updatedTeacher);
        console.log(body);

        fetch('update_teacher.php', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: body
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.success) {
                alert(data.message || 'Informațiile profesorului au fost actualizate cu succes.');
                loadTeachers();
            } else {
                alert(data.error || 'A apărut o eroare la actualizarea informațiilor profesorului.');
            }
        })
        .catch(error => {
            alert(error.error || 'A apărut o eroare la actualizarea informațiilor profesorului.');
        });
    });

    teacherNameInput.addEventListener('input', function() {
        updateTeacherButton.disabled = false;
        updateTeacherButton.classList.remove('disabled');
    });

    teacherSubjectInput.addEventListener('input', function() {
        updateTeacherButton.disabled = false;
        updateTeacherButton.classList.remove('disabled');
    });

    teacherExperienceInput.addEventListener('input', function() {
        updateTeacherButton.disabled = false;
        updateTeacherButton.classList.remove('disabled');
    });

    loadTeachers();
});