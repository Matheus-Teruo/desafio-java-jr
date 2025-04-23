-- V1__create-base-schema.sql

-- Table for books
CREATE TABLE books (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    ano_publicacao VARCHAR(255) NOT NULL
);