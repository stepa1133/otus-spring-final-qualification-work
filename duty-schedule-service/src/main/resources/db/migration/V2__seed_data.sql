-- ============================================
-- 1. Создаём начальника
-- ============================================
INSERT INTO employees (full_name, email, password, role, active)
VALUES ('Петрова Анна Сергеевна', 'anna@company.ru', '123456', 'CHIEF', true);

-- ============================================
-- 2. Создаём отделы
-- ============================================

-- Особые отделы
INSERT INTO departments (name, special, active, chief_id)
VALUES ('Бухгалтерия', true, true, 1);

INSERT INTO departments (name, special, active, chief_id)
VALUES ('Казначейство', true, true, 1);

-- Обычные отделы
INSERT INTO departments (name, special, active, chief_id)
VALUES ('ИТ-отдел', false, true, 1);

INSERT INTO departments (name, special, active, chief_id)
VALUES ('HR-отдел', false, true, 1);

INSERT INTO departments (name, special, active, chief_id)
VALUES ('Хозяйственный отдел', false, true, 1);

-- ============================================
-- 3. Создаём сотрудников
-- ============================================

-- Бухгалтерия (особый) — 4 сотрудника
INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Иванова Мария Ивановна', 'ivanova@company.ru', '123456', 'EMPLOYEE', true, 1);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Смирнова Елена Петровна', 'smirnova@company.ru', '123456', 'EMPLOYEE', true, 1);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Козлова Ольга Викторовна', 'kozlova@company.ru', '123456', 'EMPLOYEE', true, 1);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Новикова Татьяна Андреевна', 'novikova@company.ru', '123456', 'EMPLOYEE', true, 1);

-- Казначейство (особый) — 3 сотрудника
INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Морозов Дмитрий Сергеевич', 'morozov@company.ru', '123456', 'EMPLOYEE', true, 2);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Волков Алексей Игоревич', 'volkov@company.ru', '123456', 'EMPLOYEE', true, 2);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Соколов Павел Николаевич', 'sokolov@company.ru', '123456', 'EMPLOYEE', true, 2);

-- ИТ-отдел (обычный) — 3 сотрудника
INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Кузнецов Артём Валерьевич', 'kuznetsov@company.ru', '123456', 'EMPLOYEE', true, 3);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Попов Максим Александрович', 'popov@company.ru', '123456', 'EMPLOYEE', true, 3);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Васильев Игорь Романович', 'vasiliev@company.ru', '123456', 'EMPLOYEE', true, 3);

-- HR-отдел (обычный) — 2 сотрудника
INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Михайлова Светлана Юрьевна', 'mihailova@company.ru', '123456', 'EMPLOYEE', true, 4);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Фёдорова Наталья Ильинична', 'fedorova@company.ru', '123456', 'EMPLOYEE', true, 4);

-- Хозяйственный отдел (обычный) — 2 сотрудника
INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Егоров Виктор Степанович', 'egorov@company.ru', '123456', 'EMPLOYEE', true, 5);

INSERT INTO employees (full_name, email, password, role, active, department_id)
VALUES ('Алексеев Сергей Владимирович', 'alekseev@company.ru', '123456', 'EMPLOYEE', true, 5);

-- ============================================
-- 4. Добавляем отсутствия
-- ============================================

INSERT INTO absences (employee_id, type, start_date, end_date, reason)
VALUES (2, 'SICK_LEAVE', '2026-03-10', '2026-03-14', 'ОРВИ');

INSERT INTO absences (employee_id, type, start_date, end_date, reason)
VALUES (6, 'VACATION', '2026-03-20', '2026-03-25', 'Ежегодный отпуск');