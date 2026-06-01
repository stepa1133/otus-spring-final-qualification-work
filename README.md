# Система управления графиками дежурств

## Описание системы

Система автоматизирует составление графиков дежурств для бухгалтерского отдела.
Начальник составляет график на месяц, а система сама распределяет сотрудников по датам.

### Правила распределения дежурств

- Отделы бывают **особые** и **обычные**
- Из каждого особого отдела **каждый день** дежурит один человек
- Из всех обычных отделов — **один человек суммарно** в день
- Если 2 особых отдела и 3 обычных → 3 дежурных в день (2 особых + 1 обычный)
- Учитываются больничные, отпуска, отгулы
- Кто меньше дежурил — тот назначается первым

### Роли

- **CHIEF** — создаёт отделы, сотрудников, графики, отмечает отсутствия
- **EMPLOYEE** — смотрит график, оставляет заявки на перенос (в разработке)

---

## Стек

| Технология | Назначение |
|------------|-----------|
| Java 17 | Язык |
| Spring Boot 3.5 | Фреймворк |
| Spring Data JPA / Hibernate | Работа с БД |
| Spring Security | Безопасность и аутентификация |
| Spring Shell | Консольное управление |
| Spring Actuator | Мониторинг и диагностика |
| Spring Cloud OpenFeign | HTTP-клиент для межсервисного взаимодействия |
| Resilience4j | Circuit Breaker и Retry |
| Flyway | Миграции БД |
| PostgreSQL 16 | База данных |
| Docker Compose | Контейнеризация |
| Bootstrap 5 | Фронтенд |
| Lombok | Упрощение кода |
| Maven | Сборка |

## Архитектура




| Сервис | Порт | База |
|--------|------|------|
| duty-schedule-service | 8080 | duty_schedule_db |
| security-service | 8081 | security_db |
| notification-service | 8082 | notification_db |

---

## Модель данных

### Department (Отдел)

| Поле | Тип | Описание |
|------|-----|----------|
| id | Long | Идентификатор |
| name | String | Название |
| special | boolean | Особый отдел? |
| active | boolean | Активен? |
| chief | Employee | Начальник |

### Employee (Сотрудник)

| Поле | Тип | Описание |
|------|-----|----------|
| id | Long | Идентификатор |
| fullName | String | ФИО |
| email | String | Email (логин) |
| password | String | Пароль |
| role | Role | CHIEF / EMPLOYEE |
| department | Department | Отдел |
| active | boolean | Работает? |

### DutyGroup (График)

| Поле | Тип | Описание |
|------|-----|----------|
| id | Long | Идентификатор |
| name | String | Название ("Март 2026") |
| startDate | LocalDate | Начало периода |
| endDate | LocalDate | Конец периода |
| status | DutyGroupStatus | DRAFT / ACTIVE / COMPLETED |
| createdBy | Employee | Кто создал |
| duties | List\<Duty\> | Список дежурств |

### Duty (Дежурство)

| Поле | Тип | Описание |
|------|-----|----------|
| id | Long | Идентификатор |
| date | LocalDate | Дата |
| employee | Employee | Кто дежурит |
| department | Department | Из какого отдела |
| specialDuty | boolean | Из особого отдела? |
| status | DutyStatus | SCHEDULED / SUBSTITUTED / CANCELLED |
| comment | String | Комментарий |
| substitutedBy | Employee | Кто заменил |

### Absence (Отсутствие)

| Поле | Тип | Описание |
|------|-----|----------|
| id | Long | Идентификатор |
| employee | Employee | Кто отсутствует |
| type | AbsenceType | SICK_LEAVE / VACATION / TIME_OFF |
| startDate | LocalDate | Начало |
| endDate | LocalDate | Конец |
| reason | String | Причина |

---

## Быстрый старт

### 1. 
Запустить build-all.sh




### 2.
Запуск через docker compose
Переходим в корень проекта и запускаем build-all.sh

```bash
sudo docker compose up -d --build
```




## Spring Boot Actuator

Для мониторинга и диагностики подключён Spring Boot Actuator.

### Доступные эндпоинты

| URL | Описание |
|-----|----------|
| `/actuator` | Список всех доступных эндпоинтов |
| `/actuator/health` | Статус приложения, БД и зависимостей |
| `/actuator/info` | Информация о приложении |
| `/actuator/metrics` | Метрики JVM, HTTP-запросов, БД |
| `/actuator/flyway` | Статус и история миграций Flyway |
| `/actuator/beans` | Список всех Spring-бинов |
| `/actuator/mappings` | Все URL-маппинги контроллеров |
| `/actuator/env` | Переменные окружения и настройки |
| `/actuator/loggers` | Уровни логирования (чтение и изменение) |

### Примеры запросов

```bash
# Статус приложения
curl http://localhost:8080/actuator/health

# Статус миграций
curl http://localhost:8080/actuator/flyway

# Метрики HTTP-запросов
curl http://localhost:8080/actuator/metrics/http.server.requests

# Все маппинги
curl http://localhost:8080/actuator/mappings

# Список бинов
curl http://localhost:8080/actuator/beans

# Изменить уровень логирования
curl -X POST http://localhost:8080/actuator/loggers/ru.otus.dutyschedule \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "TRACE"}'