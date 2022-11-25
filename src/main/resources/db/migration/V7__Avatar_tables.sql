CREATE TABLE IF NOT EXISTS avatar
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    category     VARCHAR(255)                          NOT NULL,
    path         VARCHAR(255)                          NOT NULL,
    available    BOOLEAN   DEFAULT TRUE                NOT NULL
);
