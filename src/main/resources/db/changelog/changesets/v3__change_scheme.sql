--liquibase formatted sql
--changeset eva:3

DROP TABLE IF EXISTS students;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);


CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    fio VARCHAR(64) NOT NULL,
    group_id BIGINT,
    user_id BIGINT UNIQUE,
    phone_number VARCHAR(24)
);

CREATE TABLE teachers (
    id BIGSERIAL PRIMARY KEY,
    fio VARCHAR(64) NOT NULL,
    user_id BIGINT UNIQUE,
    phone_number VARCHAR(24),
    subject VARCHAR(64)
);

CREATE TABLE admins (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE
);

CREATE TABLE teacher_groups (
     teacher_id BIGINT,
     group_id BIGINT,
     PRIMARY KEY (teacher_id, group_id)
);

ALTER TABLE students ADD CONSTRAINT fk_students_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE students ADD CONSTRAINT fk_students_group_id
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE SET NULL;
ALTER TABLE teachers ADD CONSTRAINT fk_teachers_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE admins ADD CONSTRAINT fk_admins_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE teacher_groups ADD CONSTRAINT fk_teacher_groups_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE;
ALTER TABLE teacher_groups ADD CONSTRAINT fk_teacher_groups_group
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE;

CREATE INDEX idx_students_user_id ON students(user_id);
CREATE INDEX idx_students_group_id ON students(group_id);
CREATE INDEX idx_teachers_user_id ON teachers(user_id);
CREATE INDEX idx_admins_user_id ON admins(user_id);

INSERT INTO users (username, email, password, role, enabled, created_at, updated_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$12$1UbqTzVYzORi8awYmboEhunjeiyPjd5ijg2QOSKX5kdMevlth8Khm',
    'ROLE_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

INSERT INTO admins (user_id)
SELECT id FROM users WHERE username = 'admin'
ON CONFLICT (user_id) DO NOTHING;