-- База для основного сервиса
CREATE DATABASE duty_schedule_db;

-- База для security-сервиса
CREATE DATABASE security_db;

-- База для сервиса нотификаций
CREATE DATABASE notification_db;

-- Даём права пользователю admin на все базы
GRANT ALL PRIVILEGES ON DATABASE duty_schedule_db TO admin;
GRANT ALL PRIVILEGES ON DATABASE security_db TO admin;
GRANT ALL PRIVILEGES ON DATABASE notification_db TO admin;