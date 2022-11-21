CREATE TABLE IF NOT EXISTS player_spotlight
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player_id    INT                                   NOT NULL,
    CONSTRAINT fk_player_spotlight_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
