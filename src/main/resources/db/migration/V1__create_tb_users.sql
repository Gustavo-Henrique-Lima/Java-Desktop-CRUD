CREATE TABLE tb_users (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password_hash   VARCHAR(64)  NOT NULL,
    salt            VARCHAR(32)  NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);