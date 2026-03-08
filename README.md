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

```sql
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
```

Digite a senha (123456) e você estará dentro do MySQL:

```sql
USE workbatch_db;
SHOW TABLES;
```

## JOBS
+------------------------------+
| Tables_in_workbatch_db       |
+------------------------------+
| BATCH_JOB_EXECUTION          |
| BATCH_JOB_EXECUTION_CONTEXT  |
| BATCH_JOB_EXECUTION_PARAMS   |
| BATCH_JOB_EXECUTION_SEQ      |
| BATCH_JOB_INSTANCE           |
| BATCH_JOB_SEQ                |
| BATCH_STEP_EXECUTION         |
| BATCH_STEP_EXECUTION_CONTEXT |
| BATCH_STEP_EXECUTION_SEQ     |
+------------------------------+

Execuções com sucesso : BATCH_JOB_INSTANCE
+-----------------+---------+----------------+----------------------------------+
| JOB_INSTANCE_ID | VERSION | JOB_NAME       | JOB_KEY                          |
+-----------------+---------+----------------+----------------------------------+
|               1 |       0 | imprimirOlaJob | d41d8cd98f00b204e9800998ecf8427e |
+-----------------+---------+----------------+----------------------------------+
Quantas vezes executou ao total mesmo com falha: BATCH_JOB_EXECUTION
+------------------+---------+-----------------+----------------------------+----------------------------+----------------------------+-----------+-----------+--------------+----------------------------+
| JOB_EXECUTION_ID | VERSION | JOB_INSTANCE_ID | CREATE_TIME                | START_TIME                 | END_TIME                   | STATUS    | EXIT_CODE | EXIT_MESSAGE | LAST_UPDATED               |
+------------------+---------+-----------------+----------------------------+----------------------------+----------------------------+-----------+-----------+--------------+----------------------------+
|                1 |       2 |               1 | 2026-03-07 23:44:36.362577 | 2026-03-07 23:44:36.388478 | 2026-03-07 23:44:36.447451 | COMPLETED | COMPLETED |              | 2026-03-07 23:44:36.448031 |
+------------------+---------+-----------------+----------------------------+----------------------------+----------------------------+-----------+-----------+--------------+----------------------------+
Quais dados foram salvos no contexto de execução do job: BATCH_JOB_EXECUTION_CONTEXT
+------------------+--------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
| JOB_EXECUTION_ID | SHORT_CONTEXT                                                                                                                                    | SERIALIZED_CONTEXT |
+------------------+--------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
|                1 | rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMi40eA== | NULL               |
+------------------+--------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
Quais steps executaram: BATCH_STEP_EXECUTION

> Remover metadados do spring batch
```sql
delete from BATCH_JOB_EXECUTION_CONTEXT;
delete from BATCH_JOB_EXECUTION_PARAMS;
delete from BATCH_JOB_EXECUTION_SEQ;

delete from BATCH_STEP_EXECUTION_CONTEXT;
delete from BATCH_STEP_EXECUTION_SEQ;
delete from BATCH_STEP_EXECUTION;

delete from BATCH_JOB_EXECUTION;
delete from BATCH_JOB_INSTANCE;
delete from BATCH_JOB_SEQ;
```
- Se o processamento foi interrompido a gente pode precisar retomá-lo de onde ele parou e se esse parâmetro
for usado vai ser criado uma nova instância do job.
Ele não vai conseguir retomar aquele processamento na última instância que ele parou.
Por isso que a gente não pode sempre utilizar esse incrementando a não ser que a gente saiba que a nossa

```bash
public Job imprimirOlaJob(JobRepository jobRepository, Step stepOlaMundo) {
    return new JobBuilder("imprimirOlaJob", jobRepository)
    .start(stepOlaMundo)
    .incrementer(new RunIdIncrementer())
    .build();
}
```