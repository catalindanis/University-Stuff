$(document).ready(function() {
    const $teacherSelect = $('#teacherSelect');
    const $teacherName = $('#teacherName');
    const $teacherSubject = $('#teacherSubject');
    const $teacherExperience = $('#teacherExperience');
    const $updateTeacherButton = $('#updateTeacherButton');

    let teachersData = [];
    let selectedTeacher = null;

    function loadTeachers() {
        $.getJSON('get_teachers_edit.php')
            .done(function(data) {
                teachersData = data.teachers || [];
                renderTeacherOptions();
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                console.error('Error fetching teachers:', textStatus, errorThrown);
            });
    }

    function renderTeacherOptions() {
        $teacherSelect.empty().append($('<option>').val('').text('Alegeți un profesor'));
        $.each(teachersData, function(i, teacher) {
            $teacherSelect.append($('<option>').val(teacher.id).text(teacher.id));
        });

        selectedTeacher = null;
        updateTeacherForm();
    }

    function updateTeacherForm() {
        $teacherName.val(selectedTeacher ? selectedTeacher.name : '');
        $teacherSubject.val(selectedTeacher ? selectedTeacher.subject : '');
        $teacherExperience.val(selectedTeacher ? selectedTeacher.experience : '');

        const disabled = selectedTeacher === null;
        $teacherName.prop('disabled', disabled).toggleClass('disabled', disabled);
        $teacherSubject.prop('disabled', disabled).toggleClass('disabled', disabled);
        $teacherExperience.prop('disabled', disabled).toggleClass('disabled', disabled);
        $updateTeacherButton.prop('disabled', true).addClass('disabled');
    }

    $teacherSelect.on('change', function() {
        if ($updateTeacherButton.prop('disabled') === false) {
            const confirmSwitch = confirm('Nu ai salvat modificarile facute. Esti sigur ca vrei sa schimbi profesorul selectat?');
            if (!confirmSwitch) {
                $(this).val(selectedTeacher ? selectedTeacher.id : '');
                return;
            }
        }

        const selectedTeacherId = $(this).val();
        if (!selectedTeacherId) {
            selectedTeacher = null;
            updateTeacherForm();
            return;
        }

        $.getJSON('get_teacher_by_id_edit.php', { teacher_id: selectedTeacherId })
            .done(function(data) {
                selectedTeacher = data.teacher || null;
                updateTeacherForm();
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                console.error('Error fetching teacher details:', textStatus, errorThrown);
            });
    });

    $updateTeacherButton.on('click', function() {
        if (!selectedTeacher) {
            alert('Vă rugăm să selectați un profesor pentru a actualiza informațiile.');
            return;
        }

        const updatedTeacher = {
            id: selectedTeacher.id,
            name: $teacherName.val(),
            subject: $teacherSubject.val(),
            experience: parseInt($teacherExperience.val())
        };

        $.ajax({
            url: 'update_teacher.php',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(updatedTeacher),
            dataType: 'json'
        })
        .done(function(data) {
            if (data.success) {
                alert(data.message || 'Informațiile profesorului au fost actualizate cu succes.');
                loadTeachers();
            } else {
                alert(data.error || 'A apărut o eroare la actualizarea informațiilor profesorului.');
            }
        })
        .fail(function(jqXHR) {
            var err = null;
            try { err = jqXHR.responseJSON && jqXHR.responseJSON.error; } catch(e) { err = null; }
            alert(err || 'A apărut o eroare la actualizarea informațiilor profesorului.');
        });
    });

    $teacherName.on('input', activateSave);
    $teacherSubject.on('input', activateSave);
    $teacherExperience.on('input', activateSave);

    function activateSave() {
        $updateTeacherButton.prop('disabled', false).removeClass('disabled');
    }

    loadTeachers();
});
