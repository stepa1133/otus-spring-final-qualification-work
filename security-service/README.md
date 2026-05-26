# Postgres
## Подключение к контейнеру с БД на локальной машине
psql -h localhost -p 5431 -U admin -d security_db

## Очистка
delete from flyway_schema_history;
drop table users_roles;
drop table roles;
drop table users;

# Быстрый перезапуск
docker compose down
/bin/bash /home/igor/own/otus-spring-final-qualification-work/build-all.sh
docker compose up -d --build

# Генерация RSA PKCS8 ключей для JWT
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem