-- Основная база service1_db уже создана через POSTGRES_DB
-- Создаём дополнительные базы для других микросервисов

CREATE DATABASE duty_schedule_service_db;


-- Даём права пользователю otus на все базы
GRANT ALL PRIVILEGES ON DATABASE duty_schedule_service_db TO otus;
