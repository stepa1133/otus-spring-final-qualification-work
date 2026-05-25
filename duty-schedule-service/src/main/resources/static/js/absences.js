/**
 * Загрузить страницу отсутствий.
 */
async function loadAbsencesPage() {
    const container = document.getElementById('main-content');
    const response = await fetch('pages/absences.html');
    container.innerHTML = await response.text();

    // Загружаем списки сотрудников
    await loadEmployeeOptions();
    await loadEmployeeFilter();

    // Обработчик формы
    document.getElementById('absence-form').addEventListener('submit', createAbsence);
}

/**
 * Загрузить сотрудников в выпадающий список формы.
 */
async function loadEmployeeOptions() {
    try {
        const employees = await apiRequest('employees');
        const select = document.getElementById('absence-employee');
        select.innerHTML = '<option value="">Выберите сотрудника</option>';
        employees.forEach(emp => {
            select.innerHTML += `<option value="${emp.id}">${emp.fullName} (${emp.departmentName})</option>`;
        });
    } catch (error) {
        showError('absences-message', 'Ошибка загрузки сотрудников: ' + error.message);
    }
}

/**
 * Загрузить сотрудников в фильтр.
 */
async function loadEmployeeFilter() {
    try {
        const employees = await apiRequest('employees');
        const filter = document.getElementById('absence-employee-filter');
        filter.innerHTML = '<option value="">Выберите сотрудника</option>';
        employees.forEach(emp => {
            filter.innerHTML += `<option value="${emp.id}">${emp.fullName}</option>`;
        });
    } catch (error) {
        // игнорируем
    }
}

/**
 * Загрузить отсутствия конкретного сотрудника.
 */
async function loadAbsencesByEmployee(employeeId) {
    if (!employeeId) {
        document.getElementById('absences-list').innerHTML =
            '<p class="text-muted">Выберите сотрудника для просмотра его отсутствий</p>';
        return;
    }

    try {
        const absences = await apiRequest(`absences/employee/${employeeId}`);
        const listDiv = document.getElementById('absences-list');

        if (absences.length === 0) {
            listDiv.innerHTML = '<p class="text-muted">У сотрудника нет зарегистрированных отсутствий</p>';
            return;
        }

        const typeNames = { 'SICK_LEAVE': 'Больничный', 'VACATION': 'Отпуск', 'TIME_OFF': 'Отгул' };

        let html = '<div class="table-responsive"><table class="table table-striped">';
        html += '<thead><tr><th>ID</th><th>Тип</th><th>Начало</th><th>Конец</th><th>Причина</th></tr></thead>';
        html += '<tbody>';

        for (const abs of absences) {
            html += '<tr>';
            html += `<td>${abs.id}</td>`;
            html += `<td>${typeNames[abs.type] || abs.type}</td>`;
            html += `<td>${abs.startDate}</td>`;
            html += `<td>${abs.endDate}</td>`;
            html += `<td>${abs.reason || '-'}</td>`;
            html += '</tr>';
        }

        html += '</tbody></table></div>';
        listDiv.innerHTML = html;
    } catch (error) {
        showError('absences-message', 'Ошибка: ' + error.message);
    }
}

/**
 * Добавить отсутствие.
 */
async function createAbsence(event) {
    event.preventDefault();

    const employeeId = document.getElementById('absence-employee').value;
    const type = document.getElementById('absence-type').value;
    const startDate = document.getElementById('absence-start').value;
    const endDate = document.getElementById('absence-end').value;
    const reason = document.getElementById('absence-reason').value.trim();

    if (!employeeId || !startDate || !endDate) {
        showError('absences-message', 'Заполните обязательные поля');
        return;
    }

    if (startDate > endDate) {
        showError('absences-message', 'Дата начала не может быть позже даты окончания');
        return;
    }

    try {
        await apiRequest('absences', 'POST', {
            employeeId: parseInt(employeeId),
            type,
            startDate,
            endDate,
            reason: reason || null
        });

        const typeNames = { 'SICK_LEAVE': 'Больничный', 'VACATION': 'Отпуск', 'TIME_OFF': 'Отгул' };
        showSuccess('absences-message', `${typeNames[type]} зарегистрирован`);
        document.getElementById('absence-form').reset();

        // Обновить список
        const filterVal = document.getElementById('absence-employee-filter').value;
        if (filterVal) {
            loadAbsencesByEmployee(filterVal);
        }
    } catch (error) {
        showError('absences-message', 'Ошибка: ' + error.message);
    }
}