// Базовый URL REST API
const API_URL = 'http://localhost:8080/api';

/**
 * Универсальная функция для запросов к API.
 */
async function apiRequest(endpoint, method = 'GET', body = null) {
    const url = `${API_URL}/${endpoint}`;
    const token = localStorage.getItem('jwt_token');

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(url, options);

        // 401 — токен просрочен или невалиден → на логин
        if (response.status === 401) {
            localStorage.removeItem('jwt_token');
            localStorage.removeItem('user_role');
            localStorage.removeItem('username');
            window.location.href = '/pages/login.html';
            return;
        }

        // 403 — нет прав, просто ошибка
        if (response.status === 403) {
            throw new Error('Доступ запрещён');
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || `Ошибка ${response.status}`);
        }

        if (response.status === 204) {
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error(`Ошибка API [${method} ${endpoint}]:`, error);
        throw error;
    }
}

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