-- ============================================
-- Предустановленные пользователи
-- username = email из duty-schedule-service
-- password = 123456 (BCrypt хеш)
-- ============================================

-- Начальник
INSERT INTO users (username, password, role)
VALUES ('anna@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'CHIEF');
VALUES ('anna@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'CHIEF');

-- Бухгалтерия (особый отдел)
INSERT INTO users (username, password, role) VALUES
    ('ivanova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('smirnova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('kozlova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('novikova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE');

-- Казначейство (особый отдел)
INSERT INTO users (username, password, role) VALUES
    ('morozov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('volkov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('sokolov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE');

-- ИТ-отдел
INSERT INTO users (username, password, role) VALUES
    ('kuznetsov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('popov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('vasiliev@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE');

-- HR-отдел
INSERT INTO users (username, password, role) VALUES
    ('mihailova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('fedorova@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE');

-- Хозяйственный отдел
INSERT INTO users (username, password, role) VALUES
    ('egorov@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE'),
    ('alekseev@company.ru', '$2a$10$bB/DSzHyVslr6sAn4ZgyD.kBazMcZg1AmTEiPZ9G/2uz6ahEFzTdG', 'EMPLOYEE');