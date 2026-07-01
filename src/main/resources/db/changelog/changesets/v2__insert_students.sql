--liquibase formatted sql
--changeset eva:2
INSERT INTO students (fio, group_of_students, phone_number) VALUES
('Иванов Иван Иванович', 'Группа А', '+7(999)123-45-67'),
('Петрова Анна Сергеевна', 'Группа Б', '+7(999)234-56-78'),
('Сидоров Петр Петрович', 'Группа А', '+7(999)345-67-89');