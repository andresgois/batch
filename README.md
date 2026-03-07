# Applicação

## Banco
### 1️⃣ Rodar MySQL 8 com Docker

Use o seguinte comando:

```bash
docker run -d \
--name mysql-container \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_DATABASE=workbatch_db \
-e MYSQL_USER=andre \
-e MYSQL_PASSWORD=123456 \
-p 3306:3306 \
mysql:8.0
```

Explicação rápida:

--name mysql-container → nome do container

MYSQL_ROOT_PASSWORD → senha do root
MYSQL_DATABASE → banco inicial que será criado (workbatch_db)
MYSQL_USER e MYSQL_PASSWORD → usuário padrão que seu app vai usar
-p 3306:3306 → expõe a porta 3306 do container na máquina local

Depois de alguns segundos, o MySQL estará pronto para conexão.

### 2️⃣ Verificar se o container está rodando
docker ps

Você deve ver algo como:

CONTAINER ID   IMAGE      PORTS       NAMES
abcd1234ef56   mysql:8.0  0.0.0.0:3306->3306/tcp   mysql-container
3️⃣ Conectar o Spring Boot via application.yml

```bash
spring:
datasource:
url: jdbc:mysql://localhost:3306/workbatch_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
username: andre
password: 123456
driver-class-name: com.mysql.cj.jdbc.Driver

batch:
jdbc:
initialize-schema: always
```

Agora o Spring Boot vai se conectar diretamente ao MySQL rodando no Docker.

### 4️⃣ Opcional: acessar o MySQL via terminal
docker exec -it mysql-container mysql -u root -p

CREATE USER 'andre'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON workbatch_db.* TO 'andre'@'localhost';
FLUSH PRIVILEGES;

ALTER USER 'andre'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;

-- Cria usuário 'andre' com senha
CREATE USER 'andre'@'%' IDENTIFIED BY '123456';

-- Dá permissão total no banco workbatch_db
GRANT ALL PRIVILEGES ON workbatch_db.* TO 'andre'@'%';

-- Aplica as permissões
FLUSH PRIVILEGES;

docker exec -it mysql-container mysql -u andre -p

Digite a senha (123456) e você estará dentro do MySQL:

USE workbatch_db;
SHOW TABLES;