/**
 * Загрузить страницу отделов.
 * Вызывается при клике на "Отделы" в меню.
 */
async function loadDepartmentsPage() {
    const container = document.getElementById('main-content');
    const role = localStorage.getItem('user_role');

    // Загружаем HTML-шаблон
    const response = await fetch('pages/departments.html');
    container.innerHTML = await response.text();

    // Проверяем роль
    if (role !== 'CHIEF' && role !== 'ADMIN') {
        document.getElementById('departments-message').innerHTML = `
            <div class="alert alert-warning">
                <h4>Доступ ограничен</h4>
                <p>Этот раздел доступен только начальнику и администратору.</p>
            </div>
        `;
        return;
    }

    // Показываем контент
    const content = document.getElementById('departments-content');
    if (content) content.style.display = 'block';

    // Загружаем список отделов
    await loadDepartmentsList();

    // Вешаем обработчик на форму
    document.getElementById('department-form').addEventListener('submit', createDepartment);
}

/**
 * Загрузить и отобразить список отделов.
 */
async function loadDepartmentsList() {
    try {
        const departments = await apiRequest('departments');
        const listDiv = document.getElementById('departments-list');

        if (departments.length === 0) {
            listDiv.innerHTML = '<p class="text-muted">Нет отделов. Создайте первый отдел.</p>';
            return;
        }

        let html = '<div class="table-responsive"><table class="table table-striped">';
        html += '<thead><tr><th>ID</th><th>Название</th><th>Тип</th><th>Действия</th></tr></thead>';
        html += '<tbody>';

        for (const dept of departments) {
            html += '<tr>';
            html += `<td>${dept.id}</td>`;
            html += `<td>${dept.name}</td>`;
            html += `<td><span class="badge ${dept.special ? 'bg-warning' : 'bg-secondary'}">${dept.special ? 'Особый' : 'Обычный'}</span></td>`;
            html += '<td>';
            if (dept.special) {
                html += `<button class="btn btn-sm btn-outline-secondary" onclick="toggleSpecial(${dept.id}, false)">Сделать обычным</button>`;
            } else {
                html += `<button class="btn btn-sm btn-outline-warning" onclick="toggleSpecial(${dept.id}, true)">Сделать особым</button>`;
            }
            html += '</td>';
            html += '</tr>';
        }

        html += '</tbody></table></div>';
        listDiv.innerHTML = html;
    } catch (error) {
        showError('departments-message', 'Ошибка загрузки отделов: ' + error.message);
    }
}

/**
 * Создать новый отдел.
 */
async function createDepartment(event) {
    event.preventDefault();

    const name = document.getElementById('dept-name').value.trim();
    const special = document.getElementById('dept-special').checked;

    if (!name) {
        showError('departments-message', 'Введите название отдела');
        return;
    }

    try {
        await apiRequest('departments', 'POST', { name, special });
        showSuccess('departments-message', `Отдел "${name}" создан`);
        document.getElementById('department-form').reset();
        await loadDepartmentsList();
    } catch (error) {
        showError('departments-message', 'Ошибка создания отдела: ' + error.message);
    }
}

/**
 * Переключить признак "особый" у отдела.
 */
async function toggleSpecial(id, special) {
    try {
        await apiRequest(`departments/${id}/special?special=${special}`, 'PATCH');
        await loadDepartmentsList();
    } catch (error) {
        showError('departments-message', 'Ошибка: ' + error.message);
    }
}