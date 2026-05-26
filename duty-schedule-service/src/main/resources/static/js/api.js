// Базовый URL REST API
const API_URL = 'http://localhost:8080/api';
const AUTH_URL = 'http://localhost:8081/api/auth';

async function login(username, password) {
    const response = await fetch(`${AUTH_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
        throw new Error("Ошибка авторизации");
    }

    const data = await response.json();

    localStorage.setItem("token", data.token);
}

function logout() {
    if (!confirm("Выйти из системы?")) return;

    localStorage.removeItem("token");
    window.location.href = "login.html";
}

function isAuthenticated() {
    return !!localStorage.getItem("token");
}

/**
 * Универсальная функция для запросов к API.
 * @param {string} endpoint - путь после /api/ (например, 'departments')
 * @param {string} method - GET, POST, PUT, PATCH, DELETE
 * @param {object} body - тело запроса (для POST/PUT/PATCH)
 * @returns {Promise} - ответ от сервера
 */
async function apiRequest(endpoint, method = 'GET', body = null) {
    const url = `${API_URL}/${endpoint}`;

    const token = localStorage.getItem("token");

    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

// ❗ 401 — не авторизован → login
    if (response.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "./login.html";
        return;
    }

    // ❗ 403 — запрещено → заглушка
    if (response.status === 403) {
        showAccessDenied();
        throw new Error("Доступ запрещён");
    }

    const data = await response.json().catch(() => null);
    if (!response.ok) {
        throw new Error(data?.message || `Ошибка ${response.status}`);
    }
    return data;
}

/**
 * Показать сообщение об ошибке на странице.
 */
function showError(containerId, message) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
    }
}

/**
 * Показать сообщение об успехе на странице.
 */
function showSuccess(containerId, message) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
    }
}

function showAccessDenied() {
    document.getElementById('main-content').innerHTML = `
        <div class="d-flex justify-content-center align-items-center" style="height: 60vh;">
            <div class="text-center">
                <h1>🚫</h1>
                <h3>У вас нет прав для просмотра страницы</h3>
                <p class="text-muted">Обратитесь к администратору</p>
                <button class="btn btn-primary mt-3" onclick="window.location.href='index.html'">
                    На главную
                </button>
            </div>
        </div>
    `;
}