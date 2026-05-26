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
otus-spring-final-qualification-work/
├── docker-compose.yml
├── init/
│ └── 01-create-databases.sql
├── duty-schedule-service/ # Основной сервис (готов)
├── security-service/ # Аутентификация (в разработке)
└── notification-service/ # Уведомления (в разработке)



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
Запустить PostgreSQL

```bash
sudo docker compose up -d postgres
```

Далее запускаемся через IDEA DutyScheduleServiceApplication.java


### 2.
Запуск через docker compose
Переходим в корень проекта и запускаем build-all.sh

```bash
sudo docker compose up -d --build
```

Далее запускаемся через IDEA DutyScheduleServiceApplication.java

### Команды

department-create --name "Название" --special true/false
Создаёт новый отдел. Если указать --special true, отдел становится особым (каждый день должен дежурить один человек из этого отдела).
Пример: department-create --name "Бухгалтерия" --special true

department-list
Показывает список всех активных отделов.
Пример: department-list

department-special --id ID --special true/false
Делает отдел особым или обычным.
Пример: department-special --id 1 --special false

employee-create --fullName "ФИО" --email "email" --password "пароль" --departmentId ID
Добавляет нового сотрудника в указанный отдел.
Пример: employee-create --fullName "Иванова Мария" --email "ivanova@mail.ru" --password 123456 --departmentId 1

employee-list
Показывает список всех активных сотрудников.
Пример: employee-list

employee-by-department --departmentId ID
Показывает сотрудников конкретного отдела.
Пример: employee-by-department --departmentId 1

absence-add --employeeId ID --type SICK_LEAVE/VACATION/TIME_OFF --startDate ГГГГ-ММ-ДД --endDate ГГГГ-ММ-ДД --reason "Причина"
Добавляет отсутствие сотрудника. После добавления все активные графики, пересекающиеся с датами отсутствия, автоматически перегенерируются.
Пример: absence-add --employeeId 2 --type SICK_LEAVE --startDate 2026-03-10 --endDate 2026-03-14 --reason "ОРВИ"

schedule-create --name "Название" --startDate ГГГГ-ММ-ДД --endDate ГГГГ-ММ-ДД --chiefId ID
Создаёт новый график дежурств (пустой контейнер).
Пример: schedule-create --name "Март 2026" --startDate 2026-03-01 --endDate 2026-03-31 --chiefId 1

schedule-generate --groupId ID
Генерирует дежурства для указанного графика по правилам: из каждого особого отдела по 1 человеку в день, из всех обычных отделов 1 человек в день, с учётом отсутствий и равномерного распределения.
Пример: schedule-generate --groupId 1

schedule-view --groupId ID
Показывает график в виде таблицы с датами, дежурными и отделами.
Пример: schedule-view --groupId 1

schedule-reschedule --groupId ID
Перегенерирует график заново (например, после изменения состава сотрудников или отсутствий).
Пример: schedule-reschedule --groupId 1

schedule-list
Показывает список всех графиков.
Пример: schedule-list


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