# Sistema de gestão de funcionários

Aplicação desktop em Java Swing para cadastro gestão de funcionários.

## Stack

- **Java 21**
- **Maven** 
- **PostgreSQL** 
- **HikariCP**
- **Flyway**
- **Logback + SLF4J**
- **FlatLaf**
- **JUnit 5 + Mockito**
- **NetBeans**

## Arquitetura

Separação em camadas, sem lógica de negócio ou acesso a banco dentro dos componentes Swing:

```
com.gustavonascimento.sistema.cadastro
 ├── models/              Entidades puras, User, Employee, AuditLog
 │    └── enums/          AuditAction
 ├── daos/
 │    ├── interfaces/      Contratos de persistência
 │    └── impl/            Implementações com SQL nativo via PreparedStatement
 ├── services/             Regras de negócio, validação, orquestração de auditoria
 ├── controllers/          Ponte entre View e Service
 ├── views/                Telas Swing
 │    └── table/           TableModel próprio para a JTable de funcionários
 ├── infra/                ConnectionFactory, PasswordHasher, DatabaseMigrator
 ├── session/              UserSession 
 ├── utils/                Validators, ThemeDetector
 └── exceptions/           ValidacaoException, PersistenciaException
```

**Regra de dependência:** `views` → `controllers` → `services` → `daos` → `infra`.

## Funcionalidades

- **Cadastro e Login de usuário** validação de nome/e-mail/senha.
- **CRUD de Funcionários** nome, data de admissão, salário, status ativo/inativo. Listagem com busca por nome e filtro por status.
- **Sessão de usuário** usuário autenticado fica disponível via `UserSession`.
- **Auditoria** toda ação relevante (cadastro, login com sucesso/falha, cadastro/edição/exclusão de funcionário, logout) é registrada na tabela `tb_audit_log`, vinculada ao usuário responsável quando aplicável.
- **Log técnico**  exceções e falhas de banco são registradas em `logs/app.log`.

## Banco de dados

Tabelas (criadas automaticamente pelo Flyway na primeira execução, ver seção Setup):

| Tabela | Descrição |
|---|---|
| `tb_users` | Usuários do sistema (nome, e-mail, hash de senha, salt) |
| `tb_audit_log` | Log de auditoria de ações (referencia `tb_users` opcionalmente) |
| `tb_employees` | Funcionários (nome, data de admissão, salário, status ativo/inativo) |
| `flyway_schema_history` | Controle interno do Flyway, não editar manualmente |

Scripts de migration ficam em `src/main/resources/db/migration/`, nomeados `V1__...sql`, `V2__...sql`, etc. nunca editar um script que já rodou; sempre criar um novo `V(n+1)`.

## Setup — máquina nova

### 1. Pré-requisitos

- JDK 21
- PostgreSQL 15+
- Maven (incluso na instalação do NetBeans, ou standalone)

### 2. Configuração única do banco (exige superusuário)

Rode o script abaixo **uma vez por máquina nova**, como superusuário (`postgres`), no `psql` ou no Query Tool do pgAdmin. Ele cria o banco, o usuário da aplicação e concede os privilégios necessários, incluindo `CREATE ON SCHEMA public`.

```sql
CREATE DATABASE cadastro_db;

\c cadastro_db

CREATE USER app_user WITH PASSWORD 'defina_uma_senha_forte_aqui';
GRANT ALL PRIVILEGES ON DATABASE cadastro_db TO app_user;

GRANT CREATE ON SCHEMA public TO app_user;

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app_user;

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO app_user;
```

Depois de rodar isso, o Flyway assume o resto, toda criação de tabela a partir daqui é automática, na primeira execução da aplicação.

### 3. Credenciais da aplicação

Copie o modelo e preencha com suas credenciais reais:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edite `db.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/cadastro_db
db.user=app_user
db.password=sua_senha_aqui
db.pool.maxSize=10
```

### 4. Rodar a aplicação

Pelo NetBeans: abra o projeto e rode `Main.java`.

Pela linha de comando:
```bash
mvn compile exec:java -Dexec.mainClass="com.gustavonascimento.sistema.cadastro.Main"
```

Na primeira execução, o Flyway cria todas as tabelas automaticamente, não é necessário rodar nenhum script de schema manualmente.

## Testes

```bash
mvn test
```

Cobertura atual: `Validators`, `PasswordHasher`, `UserService`, `AuthenticationService`, `EmployeeService`, `AuditLogService`.