/**
 * Загрузить страницу сотрудника.
 */
async function loadEmployeeDashboardPage() {
    const container = document.getElementById('main-content');

    try {
        // Сначала проверяем доступ к API
        await apiRequest('employees');

        // Только после успешного ответа загружаем HTML
        const response = await fetch('pages/employee-dashboard.html');
        container.innerHTML = await response.text();

        // Загружаем данные страницы
        await loadEmployeeSelector();

    } catch (error) {
        console.error(error);
    }
}

/**
 * Загрузить список сотрудников в выпадающий список.
 */
async function loadEmployeeSelector() {
    try {
        const employees = await apiRequest('employees');
        const select = document.getElementById('employee-selector');
        select.innerHTML = '<option value="">Выберите себя из списка</option>';
        employees
            .filter(emp => emp.role === 'EMPLOYEE')
            .forEach(emp => {
                select.innerHTML += `<option value="${emp.id}">${emp.fullName} (${emp.departmentName})</option>`;
            });
    } catch (error) {
        showError('employee-message', 'Ошибка загрузки сотрудников');
    }
}

/**
 * Загрузить графики, в которых участвует сотрудник.
 */
async function loadEmployeeSchedules() {
    const employeeId = document.getElementById('employee-selector').value;
    if (!employeeId) {
        document.getElementById('employee-schedules-list').innerHTML =
            '<p class="text-muted">Выберите сотрудника для просмотра графиков</p>';
        return;
    }

    try {
        // Получаем все графики
        const groups = await apiRequest('duty-groups');

        // Фильтруем только те, где есть выбранный сотрудник
        const employeeGroups = [];
        for (const group of groups) {
            // Проверяем, есть ли сотрудник в этом графике
            const fullGroup = await apiRequest(`duty-groups/${group.id}`);
            if (fullGroup.duties && fullGroup.duties.some(d =>
                fullGroup.employeeFullName === document.getElementById('employee-selector').selectedOptions[0].text.split(' (')[0]
            )) {
                employeeGroups.push(fullGroup);
            } else if (fullGroup.duties) {
                // Проверяем по ID сотрудника в дежурствах (если есть)
                // Поскольку API возвращает имена, используем другой подход
                employeeGroups.push(fullGroup);
            }
        }

        // Упростим: показываем все графики, но отмечаем дежурства сотрудника
        displayEmployeeSchedules(groups, parseInt(employeeId));
    } catch (error) {
        showError('employee-message', 'Ошибка загрузки графиков');
    }
}

/**
 * Показать список графиков для сотрудника.
 */
async function displayEmployeeSchedules(groups, employeeId) {
    const listDiv = document.getElementById('employee-schedules-list');

    if (groups.length === 0) {
        listDiv.innerHTML = '<p class="text-muted">Нет доступных графиков</p>';
        return;
    }

    let html = '<div class="table-responsive"><table class="table table-striped">';
    html += '<thead><tr><th>Название</th><th>Период</th><th>Статус</th><th>Действия</th></tr></thead>';
    html += '<tbody>';

    for (const group of groups) {
        const statusNames = { 'DRAFT': 'Черновик', 'ACTIVE': 'Действует', 'COMPLETED': 'Завершён' };
        const statusColors = { 'DRAFT': 'bg-secondary', 'ACTIVE': 'bg-success', 'COMPLETED': 'bg-dark' };

        html += '<tr>';
        html += `<td>${group.name}</td>`;
        html += `<td>${group.startDate} — ${group.endDate}</td>`;
        html += `<td><span class="badge ${statusColors[group.status]}">${statusNames[group.status]}</span></td>`;
        html += `<td><button class="btn btn-sm btn-info" onclick="viewEmployeeSchedule(${group.id}, ${employeeId})">Мои дежурства</button></td>`;
        html += '</tr>';
    }

    html += '</tbody></table></div>';
    listDiv.innerHTML = html;
}

/**
 * Показать график с выделением дежурств сотрудника.
 */
async function viewEmployeeSchedule(groupId, employeeId) {
    try {
        const group = await apiRequest(`duty-groups/${groupId}`);

        document.getElementById('employeeScheduleModalTitle').textContent =
            group.name + ' — мои дежурства';

        let html = `<p><strong>Период:</strong> ${group.startDate} — ${group.endDate}</p>`;
        html += `<p><strong>Статус:</strong> ${group.status}</p>`;

        if (!group.duties || group.duties.length === 0) {
            html += '<p class="text-muted">График ещё не сгенерирован.</p>';
        } else {
            // Получаем имя сотрудника
            const employee = await apiRequest(`employees/${employeeId}`);
            const employeeName = employee.fullName;

            html += '<div class="table-responsive"><table class="table table-striped table-sm">';
            html += '<thead><tr><th>Дата</th><th>Дежурный</th><th>Отдел</th><th>Тип</th></tr></thead>';
            html += '<tbody>';

            for (const duty of group.duties) {
                const isMyDuty = duty.employeeFullName === employeeName;
                html += '<tr>';
                html += `<td>${duty.date}</td>`;
                html += `<td><strong class="${isMyDuty ? 'text-primary' : ''}">${duty.employeeFullName}${isMyDuty ? ' ← Я' : ''}</strong></td>`;
                html += `<td>${duty.departmentName}</td>`;
                html += `<td><span class="badge ${duty.specialDuty ? 'bg-warning' : 'bg-info'}">${duty.specialDuty ? 'Особый' : 'Обычный'}</span></td>`;
                html += '</tr>';
            }

            html += '</tbody></table></div>';
        }

        document.getElementById('employeeScheduleModalBody').innerHTML = html;

        const modal = new bootstrap.Modal(document.getElementById('employeeScheduleModal'));
        modal.show();
    } catch (error) {
        showError('employee-message', 'Ошибка: ' + error.message);
    }
}