--liquibase formatted sql
--changeset eva:1
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    fio VARCHAR(64) NOT NULL,
    group_of_students VARCHAR(64),
    phone_number VARCHAR(24)
);