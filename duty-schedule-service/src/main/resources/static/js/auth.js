// ========================================
// Файл: js/auth.js
// ========================================

const SECURITY_SERVICE_URL = 'http://localhost:8081';
const currentPage = window.location.pathname;

// Проверка авторизации при загрузке страницы
function checkAuth() {
    const token = localStorage.getItem('jwt_token');

    if (!token) {
        window.location.href = '/pages/login.html';
        return false;
    }
    return true;
}

// Функция логина
async function login(username, password) {
    try {
        const response = await fetch(`${SECURITY_SERVICE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error('Неверный логин или пароль');
        }

        const data = await response.json();

        localStorage.setItem('jwt_token', data.token);
        localStorage.setItem('user_role', data.role);
        localStorage.setItem('username', data.username);

        // Редирект в зависимости от роли
        if (data.role === 'EMPLOYEE') {
            window.location.href = '/pages/employee-dashboard.html';
        } else {
            window.location.href = '/index.html';
        }

    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// Функция выхода
function logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_role');
    localStorage.removeItem('username');
    window.location.href = '/pages/login.html';
}

// Обёртка для fetch с автоматическим добавлением токена
async function authFetch(url, options = {}) {
    const token = localStorage.getItem('jwt_token');

    if (!token) {
        window.location.href = '/pages/login.html';
        return;
    }

    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    return fetch(url, options);
}

// Запускаем проверку только НЕ на login.html
if (!currentPage.includes('login.html')) {
    checkAuth();
}