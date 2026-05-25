/**
 * Загрузить страницу графиков.
 */
async function loadSchedulesPage() {
    const container = document.getElementById('main-content');
    const response = await fetch('pages/schedules.html');
    container.innerHTML = await response.text();

    await loadChiefOptions();
    await loadSchedulesList();

    document.getElementById('schedule-form').addEventListener('submit', createSchedule);
}

async function loadChiefOptions() {
    try {
        const employees = await apiRequest('employees');
        const select = document.getElementById('schedule-chief');
        select.innerHTML = '<option value="">Выберите начальника</option>';
        employees.forEach(emp => {
            if (emp.role === 'CHIEF') {
                select.innerHTML += `<option value="${emp.id}">${emp.fullName}</option>`;
            }
        });
    } catch (error) {
        showError('schedules-message', 'Ошибка загрузки: ' + error.message);
    }
}

async function loadSchedulesList() {
    try {
        const groups = await apiRequest('duty-groups');
        const listDiv = document.getElementById('schedules-list');

        if (groups.length === 0) {
            listDiv.innerHTML = '<p class="text-muted">Нет графиков. Создайте первый график.</p>';
            return;
        }

        const statusNames = { 'DRAFT': 'Черновик', 'ACTIVE': 'Действует', 'COMPLETED': 'Завершён' };
        const statusColors = { 'DRAFT': 'bg-secondary', 'ACTIVE': 'bg-success', 'COMPLETED': 'bg-dark' };

        let html = '<div class="table-responsive"><table class="table table-striped">';
        html += '<thead><tr><th>ID</th><th>Название</th><th>Период</th><th>Статус</th><th>Дежурств</th><th>Действия</th></tr></thead>';
        html += '<tbody>';

        for (const group of groups) {
            html += '<tr>';
            html += `<td>${group.id}</td>`;
            html += `<td>${group.name}</td>`;
            html += `<td>${group.startDate} — ${group.endDate}</td>`;
            html += `<td><span class="badge ${statusColors[group.status]}">${statusNames[group.status]}</span></td>`;
            html += `<td>${group.duties ? group.duties.length : 0}</td>`;
            html += '<td>';
            html += `<button class="btn btn-sm btn-info me-1" onclick="viewSchedule(${group.id})">Смотреть</button>`;
            if (group.duties && group.duties.length === 0) {
                html += `<button class="btn btn-sm btn-success me-1" onclick="generateSchedule(${group.id})">Сгенерировать</button>`;
            }
            html += `<button class="btn btn-sm btn-warning me-1" onclick="rescheduleSchedule(${group.id})">Перегенерировать</button>`;
            html += `<button class="btn btn-sm btn-danger" onclick="deleteSchedule(${group.id})">Удалить</button>`;
            html += '</td>';
            html += '</tr>';
        }

        html += '</tbody></table></div>';
        listDiv.innerHTML = html;
    } catch (error) {
        showError('schedules-message', 'Ошибка загрузки графиков: ' + error.message);
    }
}

async function createSchedule(event) {
    event.preventDefault();

    const name = document.getElementById('schedule-name').value.trim();
    const startDate = document.getElementById('schedule-start').value;
    const endDate = document.getElementById('schedule-end').value;
    const chiefId = document.getElementById('schedule-chief').value;

    if (!name || !startDate || !endDate || !chiefId) {
        showError('schedules-message', 'Заполните все поля');
        return;
    }

    if (startDate > endDate) {
        showError('schedules-message', 'Дата начала не может быть позже даты окончания');
        return;
    }

    try {
        const params = new URLSearchParams({ chiefId });
        await apiRequest(`duty-groups?${params}`, 'POST', {
            name,
            startDate,
            endDate
        });
        showSuccess('schedules-message', `График "${name}" создан`);
        document.getElementById('schedule-form').reset();
        await loadSchedulesList();
    } catch (error) {
        showError('schedules-message', 'Ошибка: ' + error.message);
    }
}

async function generateSchedule(groupId) {
    try {
        await apiRequest(`duty-groups/${groupId}/generate`, 'POST');
        showSuccess('schedules-message', 'График сгенерирован!');
        await loadSchedulesList();
    } catch (error) {
        showError('schedules-message', 'Ошибка генерации: ' + error.message);
    }
}

async function rescheduleSchedule(groupId) {
    if (!confirm('Перегенерировать график? Все текущие дежурства будут заменены.')) {
        return;
    }
    try {
        await apiRequest(`duty-groups/${groupId}/reschedule`, 'POST');
        showSuccess('schedules-message', 'График перегенерирован!');
        await loadSchedulesList();
    } catch (error) {
        showError('schedules-message', 'Ошибка: ' + error.message);
    }
}

async function deleteSchedule(groupId) {
    if (!confirm('Удалить график? Все дежурства будут удалены.')) return;
    try {
        await apiRequest(`duty-groups/${groupId}`, 'DELETE');
        showSuccess('schedules-message', 'График удалён');
        await loadSchedulesList();
    } catch (error) {
        showError('schedules-message', 'Ошибка: ' + error.message);
    }
}

async function viewSchedule(groupId) {
    try {
        const group = await apiRequest(`duty-groups/${groupId}`);

        document.getElementById('scheduleModalTitle').textContent = group.name;

        let html = `<p><strong>Период:</strong> ${group.startDate} — ${group.endDate}</p>`;
        html += `<p><strong>Статус:</strong> ${group.status}</p>`;
        html += `<p><strong>Создал:</strong> ${group.createdByFullName}</p>`;

        if (!group.duties || group.duties.length === 0) {
            html += '<p class="text-muted">График ещё не сгенерирован.</p>';
        } else {
            html += '<div class="table-responsive"><table class="table table-striped table-sm">';
            html += '<thead><tr><th>Дата</th><th>Дежурный</th><th>Отдел</th><th>Тип</th></tr></thead>';
            html += '<tbody>';

            for (const duty of group.duties) {
                html += '<tr>';
                html += `<td>${duty.date}</td>`;
                html += `<td>${duty.employeeFullName}</td>`;
                html += `<td>${duty.departmentName}</td>`;
                html += `<td><span class="badge ${duty.specialDuty ? 'bg-warning' : 'bg-info'}">${duty.specialDuty ? 'Особый' : 'Обычный'}</span></td>`;
                html += '</tr>';
            }

            html += '</tbody></table></div>';
        }

        document.getElementById('scheduleModalBody').innerHTML = html;

        const modal = new bootstrap.Modal(document.getElementById('scheduleModal'));
        modal.show();
    } catch (error) {
        showError('schedules-message', 'Ошибка: ' + error.message);
    }
}