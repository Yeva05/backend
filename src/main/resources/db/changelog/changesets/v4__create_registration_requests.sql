--liquibase formatted sql
--changeset eva:4

CREATE TABLE IF NOT EXISTS registration_requests (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL, -- допустимые значения: STUDENT, TEACHER
    fio VARCHAR(255),
    phone_number VARCHAR(50),
    group_id BIGINT, -- ссылка на таблицу groups (внешний ключ опционально)
    subject VARCHAR(255),
    token VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    processed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Индексы для ускорения поиска
CREATE INDEX idx_registration_requests_token ON registration_requests(token);
CREATE INDEX idx_registration_requests_email ON registration_requests(email);
CREATE INDEX idx_registration_requests_expiry_date ON registration_requests(expiry_date);
