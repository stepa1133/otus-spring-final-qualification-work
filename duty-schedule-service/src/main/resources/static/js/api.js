// Базовый URL REST API
const API_URL = 'http://localhost:8080/api';

/**
 * Универсальная функция для запросов к API.
 * @param {string} endpoint - путь после /api/ (например, 'departments')
 * @param {string} method - GET, POST, PUT, PATCH, DELETE
 * @param {object} body - тело запроса (для POST/PUT/PATCH)
 * @returns {Promise} - ответ от сервера
 */
const JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTc3OTgwMzM2NiwiaWF0IjoxNzc5ODAzMDY2LCJyb2xlcyI6WyJST0xFX0NISUVGIl19.qNIK1HrvAzDlGCEMBPUUNOpGqImoqKmtEuLcjhMAKQqGh-HJaAQTIZNVVqXQvcTE0_iitBXOJTW7WIY16vY4ZIzVyMXEKL-lutDAg8EyqGvcAuH9wAROpC7FTWR_17k1US8XmYnwSlMPEB-dx1IHPsbi6wHoJ-dpmsQPQ3vwPbJLyOP8HenmZZyvxt2xdw1ooYHKoe-13_Ry9gCg-Zxq1HjZVCM7b9KR-LHtRn0q9hRZ7l7TpwiEtFrzIYVOg6nGY6l0_0Iao5w-O8naKatyZ4d31DXLU2HF_FJv4Lxs1KVV6Ons_ul4BzzQw-6vrHcWckNXNc_LJRsNQf2T-dZtkw";

async function apiRequest(endpoint, method = 'GET', body = null) {
    const url = `${API_URL}/${endpoint}`;

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${JWT}`
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