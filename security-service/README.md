# Postgres
## Подключение к контейнеру с БД на локальной машине
psql -h localhost -p 5431 -U admin -d security_db

# Генерация RSA PKCS8 ключей для JWT
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:4096
openssl rsa -pubout -in private.pem -out public.pem