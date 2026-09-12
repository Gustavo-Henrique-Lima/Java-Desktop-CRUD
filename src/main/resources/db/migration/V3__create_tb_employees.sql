CREATE TABLE tb_employees (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    admission_date  DATE NOT NULL,
    salary          NUMERIC(10,2) NOT NULL CHECK (salary >= 0),
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);