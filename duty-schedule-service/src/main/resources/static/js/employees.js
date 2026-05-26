/**
 * Загрузить страницу сотрудников.
 */
async function loadEmployeesPage() {
    const container = document.getElementById('main-content');

    try {
        // Проверяем ВСЕ нужные API заранее
        await Promise.all([
            apiRequest('employees'),
            apiRequest('departments')
        ]);

        // Только после успешного ответа загружаем HTML
        const response = await fetch('pages/employees.html');
        container.innerHTML = await response.text();

        // Загружаем данные страницы
        await loadDepartmentOptions();
        await loadEmployeesList();
        await loadDepartmentFilter();

        // Вешаем обработчики
        document
            .getElementById('employee-form')
            .addEventListener('submit', createEmployee);

        document
            .getElementById('employee-filter')
            .addEventListener('change', function () {
                loadEmployeesList(this.value);
            });
    } catch (error) {
        console.error(error);
    }
}

async function loadDepartmentOptions() {
    try {
        const departments = await apiRequest('departments');
        const select = document.getElementById('emp-department');
        select.innerHTML = '<option value="">Выберите отдел</option>';
        departments.forEach(dept => {
            select.innerHTML += `<option value="${dept.id}">${dept.name}${dept.special ? ' (особый)' : ''}</option>`;
        });
    } catch (error) {
        showError('employees-message', 'Ошибка загрузки отделов: ' + error.message);
    }
}

async function loadDepartmentFilter() {
    try {
        const departments = await apiRequest('departments');
        const filter = document.getElementById('employee-filter');
        filter.innerHTML = '<option value="all">Все отделы</option>';
        departments.forEach(dept => {
            filter.innerHTML += `<option value="${dept.id}">${dept.name}</option>`;
        });
    } catch (error) {
        // игнорируем
    }
}

async function loadEmployeesList(departmentId = 'all') {
    try {
        let employees;
        if (departmentId === 'all') {
            employees = await apiRequest('employees');
        } else {
            employees = await apiRequest(`employees/by-department/${departmentId}`);
        }

        const listDiv = document.getElementById('employees-list');

        if (employees.length === 0) {
            listDiv.innerHTML = '<p class="text-muted">Нет сотрудников.</p>';
            return;
        }

        let html = '<div class="table-responsive"><table class="table table-striped">';
        html += '<thead><tr><th>ID</th><th>ФИО</th><th>Email</th><th>Отдел</th><th>Роль</th><th>Действия</th></tr></thead>';
        html += '<tbody>';

        for (const emp of employees) {
            html += '<tr>';
            html += `<td>${emp.id}</td>`;
            html += `<td>${emp.fullName}</td>`;
            html += `<td>${emp.email}</td>`;
            html += `<td>${emp.departmentName}</td>`;
            html += `<td><span class="badge ${emp.role === 'CHIEF' ? 'bg-primary' : 'bg-secondary'}">${emp.role === 'CHIEF' ? 'Начальник' : 'Сотрудник'}</span></td>`;
            html += '<td>';
            html += `<button class="btn btn-sm btn-danger" onclick="deleteEmployee(${emp.id})">Уволить</button>`;
            html += '</td>';
            html += '</tr>';
        }

        html += '</tbody></table></div>';
        listDiv.innerHTML = html;
    } catch (error) {
        showError('employees-message', 'Ошибка загрузки сотрудников: ' + error.message);
    }
}

async function createEmployee(event) {
    event.preventDefault();

    const fullName = document.getElementById('emp-fullname').value.trim();
    const email = document.getElementById('emp-email').value.trim();
    const password = document.getElementById('emp-password').value;
    const departmentId = document.getElementById('emp-department').value;

    if (!fullName || !email || !password || !departmentId) {
        showError('employees-message', 'Заполните все поля');
        return;
    }

    try {
        await apiRequest('employees', 'POST', {
            fullName,
            email,
            password,
            departmentId: parseInt(departmentId)
        });
        showSuccess('employees-message', `Сотрудник "${fullName}" добавлен`);
        document.getElementById('employee-form').reset();
        await loadEmployeesList(document.getElementById('employee-filter').value);
    } catch (error) {
        showError('employees-message', 'Ошибка: ' + error.message);
    }
}

async function deleteEmployee(id) {
    if (!confirm('Уволить сотрудника?')) return;
    try {
        await apiRequest(`employees/${id}`, 'DELETE');
        showSuccess('employees-message', 'Сотрудник уволен');
        await loadEmployeesList(document.getElementById('employee-filter').value);
    } catch (error) {
        showError('employees-message', 'Ошибка: ' + error.message);
    }
}