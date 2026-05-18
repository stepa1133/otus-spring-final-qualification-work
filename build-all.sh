#!/bin/bash

# Цвета для вывода
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Список папок с сервисами
SERVICES=(
    "duty-schedule-service"
    "security-service"
    "notification-service"
)

# Функция сборки одного сервиса
build_service() {
    local service=$1
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW}Собираем: ${service}${NC}"
    echo -e "${YELLOW}========================================${NC}"

    if [ -d "$service" ]; then
        cd "$service" || exit
        mvn clean package -DskipTests

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ ${service} собран успешно${NC}"
        else
            echo -e "${RED}✗ Ошибка сборки ${service}${NC}"
            cd ..
            exit 1
        fi

        cd ..
    else
        echo -e "${RED}✗ Папка ${service} не найдена${NC}"
    fi
    echo ""
}

# Основной цикл
echo -e "${GREEN}Запуск сборки всех сервисов...${NC}"
echo ""

for service in "${SERVICES[@]}"; do
    build_service "$service"
done

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Все сервисы собраны успешно!${NC}"
echo -e "${GREEN}========================================${NC}"

# Показать собранные jar
echo ""
echo -e "${YELLOW}Собранные jar-файлы:${NC}"
for service in "${SERVICES[@]}"; do
    jar_file=$(ls "$service"/target/*.jar 2>/dev/null | head -1)
    if [ -n "$jar_file" ]; then
        echo "  ${service}: $(basename "$jar_file") ($(du -h "$jar_file" | cut -f1))"
    fi
done