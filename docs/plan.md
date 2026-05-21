# Разделение задач

### Саша
- duty-schedule-service

### Игорь
- notification-service
- security-service

# Описание сервисов

## Security-service

### Требования к сеорвису
1. Access token - JWT. Содержит:
- роли
- exp

2. Система ролей:
- admin
- user