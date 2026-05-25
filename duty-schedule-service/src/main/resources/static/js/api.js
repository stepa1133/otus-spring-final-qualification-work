// Базовый URL REST API
const API_URL = 'http://localhost:8080/api';

/**
 * Универсальная функция для запросов к API.
 * @param {string} endpoint - путь после /api/ (например, 'departments')
 * @param {string} method - GET, POST, PUT, PATCH, DELETE
 * @param {object} body - тело запроса (для POST/PUT/PATCH)
 * @returns {Promise} - ответ от сервера
 */
async function apiRequest(endpoint, method = 'GET', body = null) {
    const url = `${API_URL}/${endpoint}`;

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || `Ошибка ${response.status}`);
        }

        // Если ответ пустой (например, DELETE)
        if (response.status === 204) {
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error(`Ошибка API [${method} ${endpoint}]:`, error);
        throw error;
    }
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