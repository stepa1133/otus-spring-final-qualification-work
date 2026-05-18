# Ты только что изменил код → нужно пересобрать
# Собрать все сервисы
cd duty-schedule-service && mvn clean package -DskipTests && cd ..
cd security-service && mvn clean package -DskipTests && cd ..
cd notification-service && mvn clean package -DskipTests && cd ..

# Запустить всё
sudo docker compose up -d --build

# Код не менялся, просто перезапускаешь
sudo docker compose up -d

# Менял только docker-compose.yml (не код сервиса)
sudo docker compose up -d    # --build не нужен

# Менял и код, и compose-файл
sudo docker compose up -d --build

# Если база уже существует — удалить том для чистой инициализации
sudo docker compose down -v

Сервис               	Внешний порт	База данных
duty-schedule-service	8080	        duty_schedule_db
security-service	    8081	        security_db
notification-service	8082	        notification_db
postgres	            5432	        postgres_db


# Список всех контейнеров
sudo docker compose ps

# Логи конкретного сервиса
sudo docker compose logs -f notification-service

# Логи всех сервисов
sudo docker compose logs -f