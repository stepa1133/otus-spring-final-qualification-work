# Базовый образ с JDK
FROM eclipse-temurin:17-jre-alpine

# Рабочая папка внутри контейнера
WORKDIR /app

# Копируем собранный jar внутрь контейнера
COPY target/*.jar app.jar

# Порт, который слушает приложение
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]