# Ты только что изменил код → нужно пересобрать
mvn clean package -DskipTests
sudo docker compose up -d --build

# Код не менялся, просто перезапускаешь
sudo docker compose up -d

# Менял только docker-compose.yml (не код сервиса)
sudo docker compose up -d    # --build не нужен

# Менял и код, и compose-файл
sudo docker compose up -d --build