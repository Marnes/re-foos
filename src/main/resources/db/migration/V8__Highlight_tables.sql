CREATE TABLE IF NOT EXISTS highlight_message
(
    id
    SERIAL
    PRIMARY
    KEY,
    created_date
    TIMESTAMP
    DEFAULT
(
    CURRENT_TIMESTAMP
) NOT NULL, updated_date TIMESTAMP NOT NULL, "type" VARCHAR
(
    255
) NOT NULL, message VARCHAR
(
    1000
) NOT NULL);
CREATE TABLE IF NOT EXISTS highlight
(
    id
    SERIAL
    PRIMARY
    KEY,
    created_date
    TIMESTAMP
    DEFAULT
(
    CURRENT_TIMESTAMP
) NOT NULL, updated_date TIMESTAMP NOT NULL, match_id INT NOT NULL, game_id INT NULL, highlight_message_id INT NOT NULL, CONSTRAINT fk_highlight_match_id__id FOREIGN KEY
(
    match_id
) REFERENCES "match"
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_highlight_game_id__id FOREIGN KEY
(
    game_id
) REFERENCES game
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_highlight_highlight_message_id__id FOREIGN KEY
(
    highlight_message_id
) REFERENCES highlight_message
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT);
CREATE TABLE IF NOT EXISTS highlight_player
(
    id
    SERIAL
    PRIMARY
    KEY,
    created_date
    TIMESTAMP
    DEFAULT
(
    CURRENT_TIMESTAMP
) NOT NULL, updated_date TIMESTAMP NOT NULL, highlight_id INT NOT NULL, player_id INT NOT NULL, "result" VARCHAR
(
    100
) NOT NULL, CONSTRAINT fk_highlight_player_highlight_id__id FOREIGN KEY
(
    highlight_id
) REFERENCES highlight
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_highlight_player_player_id__id FOREIGN KEY
(
    player_id
) REFERENCES player
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT);

