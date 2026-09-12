CREATE TABLE tb_audit_log (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES tb_users(id),
    action      VARCHAR(100) NOT NULL,
    details     VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);