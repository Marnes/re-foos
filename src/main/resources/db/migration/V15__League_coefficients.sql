CREATE TABLE IF NOT EXISTS league_coefficients
(
    id                 SERIAL PRIMARY KEY,
    created_date       TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date       TIMESTAMP                             NOT NULL,
    league_id          INT                                   NOT NULL,
    k_value            DOUBLE PRECISION                      NOT NULL,
    result_coefficient INT                                   NOT NULL,
    CONSTRAINT fk_league_coefficients_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE league_coefficients
    ADD CONSTRAINT league_coefficients_unique_idx UNIQUE (league_id);

INSERT INTO league_coefficients(created_date, updated_date, league_id, k_value, result_coefficient)
SELECT now(), now(), id, 36, 50 FROM league;
