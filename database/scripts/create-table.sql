CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NULL,
    person_id VARCHAR(12) NOT NULL,
    uuid VARCHAR(36) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_unique_person_id UNIQUE (person_id),
    CONSTRAINT users_unique_uuid UNIQUE (uuid)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
