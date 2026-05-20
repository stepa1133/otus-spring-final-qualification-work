У меня есть знакомая, которая начальник бухгалтерского отдела.  Каждые две недели она составляет графики дежурств своих подчиненных. Дежурство - когда работник берет помимо основной своей работы дополнительную. Она хочет этот процесс автоматизировать. Работники, для которых она составляет графики из разных подразделений. Условно, среди подразделений есть особые подразделения/подразделение. Суть в том, что из особого подразделений/подразделения каждый день обязательно должен дежурить человек. А из обычных подразделений, вне зависимости от их кол-ва должен выделяться один человек. То есть если есть 5 подразделений, из них 2 особых подразделения, то дежурить должны 3 человека, по одному из особого подразделения и один из обычного подразделения.  Такой график дежурств можно составлять на месяц вперед. При этом  надо учитывать тот факт, что сотрудники могут брать больничные/отгулы/отпуска, просто увольняться. То есть график дежурств надо уметь пересоставлять динамически. При этом я хочу, чтобы у моей подруги была роль начальника и она могла составлять группы, помечать некоторые как особенные, добавлять/удалять работников. Работники в свою очередь могут смотреть на график дежурств, может оставлять комментарий на рассмотрение начальнику о переносе дежурства
1. Enums — перечисления (Начальник, обычный сотрудник)

2. Model — JPA сущности (основа данных)

3. DTO — объекты для передачи данных

4. Repository — доступ к БД

5. Exception — кастомные ошибки

6. Mapper — преобразование Entity ↔ DTO

7. Service — бизнес-логика

8. Controller — REST API

Класс	    Таблица	          Зачем
Department	departments	      Отделы, флаг special
Employee	employees	      Сотрудники-пользователи
DutyGroup	duty_groups	      График на месяц
Duty	    duties	          Конкретное дежурство
Absence	    absences	      Больничные/отпуска


Department ──1:N──→ Employee
Department ──1:N──→ Duty
Employee   ──1:N──→ Duty (кто дежурит)
Employee   ──1:N──→ Duty (кто заменил)
Employee   ──1:N──→ Absence
Employee   ──1:N──→ DutyGroup (создал)
DutyGroup  ──1:N──→ Duty


text
dto/
├── request/           ← то, что приходит от клиента
│   ├── DepartmentRequest.java
│   ├── EmployeeRequest.java
│   ├── DutyGroupRequest.java
│   ├── AbsenceRequest.java
│   └── CommentRequest.java
│
└── response/          ← то, что возвращаем клиенту
├── DepartmentResponse.java
├── EmployeeResponse.java
├── DutyResponse.java
├── DutyGroupResponse.java
└── ErrorResponse.java
Правила
Request — только то, что нужно для создания/обновления

Response — только то, что нужно клиенту, никаких паролей и лишних связей

Вместо целых объектов — departmentName вместо Department department

Вместо ID — осмысленные имена (employeeFullName вместо employeeId)



Исключение	HTTP-статус	Когда
DepartmentNotFoundException	404	Отдел не найден
EmployeeNotFoundException	404	Сотрудник не найден
DutyGroupNotFoundException	404	График не найден
NotEnoughEmployeesException	409	Нельзя составить график
DuplicateEmailException	409	Email уже занят
MethodArgumentNotValidException	400	Не прошло @Valid
Exception (общий)	500	Что-то пошло не так

DutyGroupServiceImpl.create() — создаёт пустую группу (черновик)
DutyGroupServiceImpl.generateSchedule() — вызывает генератор
↓
DutyScheduleGeneratorImpl.generate()
- На каждый день периода:
- Для каждого особого отдела → найти 1 сотрудника
- Для всех обычных отделов → найти 1 сотрудника
- Исключить отсутствующих
- Выбрать того, кто меньше дежурил
↓
Сохраняем список Duty в DutyGroup



Сводка API
Метод	URL	Кто	Что делает
POST	/api/departments	CHIEF	Создать отдел
PUT	/api/departments/{id}	CHIEF	Обновить отдел
GET	/api/departments	Все	Список отделов
GET	/api/departments/special	Все	Особые отделы
PATCH	/api/departments/{id}/special	CHIEF	Переключить особость
DELETE	/api/departments/{id}	CHIEF	Удалить отдел
POST	/api/employees	CHIEF	Добавить сотрудника
GET	/api/employees	Все	Список сотрудников
GET	/api/employees/{id}	Все	Сотрудник по ID
DELETE	/api/employees/{id}	CHIEF	Уволить
POST	/api/absences	CHIEF	Добавить отсутствие
GET	/api/absences/employee/{id}	Все	Отсутствия сотрудника
POST	/api/duty-groups	CHIEF	Создать график
POST	/api/duty-groups/{id}/generate	CHIEF	Сгенерировать график
POST	/api/duty-groups/{id}/reschedule	CHIEF	Перегенерировать
GET	/api/duty-groups/{id}	Все	Посмотреть график
PATCH	/api/duty-groups/{id}/activate	CHIEF	Активировать
PUT	/api/duties/{id}/comment	EMPLOYEE	Оставить заявку на перенос
