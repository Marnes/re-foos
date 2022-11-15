CREATE TABLE IF NOT EXISTS "user"
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, username VARCHAR
(
    255
) NOT NULL, "password" VARCHAR
(
    255
) NOT NULL);

ALTER TABLE "user"
    ADD CONSTRAINT player_username_uniq_idx UNIQUE (username);

CREATE TABLE IF NOT EXISTS player
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, user_id INT NOT NULL, avatar VARCHAR
(
    255
) NOT NULL, CONSTRAINT fk_player_user_id__id FOREIGN KEY
(
    user_id
) REFERENCES "user"
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT);

ALTER TABLE player
    ADD CONSTRAINT player_user_uniq_idx UNIQUE (user_id);

CREATE TABLE IF NOT EXISTS league
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, created_by INT NOT NULL, "name" VARCHAR
(
    255
) NOT NULL, CONSTRAINT fk_league_created_by__id FOREIGN KEY
(
    created_by
) REFERENCES "user"
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT);

CREATE TABLE IF NOT EXISTS "match"
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, created_by INT NOT NULL, league_id INT NOT NULL, CONSTRAINT fk_match_created_by__id FOREIGN KEY
(
    created_by
) REFERENCES "user"
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_match_league_id__id FOREIGN KEY
(
    league_id
) REFERENCES league
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT);

CREATE TABLE IF NOT EXISTS game
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, match_id INT NOT NULL, left_player_1 INT NOT NULL, left_player_2 INT NOT NULL, right_player_1 INT NOT NULL, right_player_2 INT NOT NULL, left_score_1 INT NOT NULL, left_score_2 INT NOT NULL, right_score_1 INT NOT NULL, right_score_2 INT NOT NULL, CONSTRAINT fk_game_match_id__id FOREIGN KEY
(
    match_id
) REFERENCES "match"
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_game_left_player_1__id FOREIGN KEY
(
    left_player_1
) REFERENCES player
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_game_left_player_2__id FOREIGN KEY
(
    left_player_2
) REFERENCES player
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_game_right_player_1__id FOREIGN KEY
(
    right_player_1
) REFERENCES player
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_game_right_player_2__id FOREIGN KEY
(
    right_player_2
) REFERENCES player
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT);

CREATE TABLE IF NOT EXISTS player_elo
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, player INT NOT NULL, match_id INT NOT NULL, change REAL NOT NULL, elo REAL NOT NULL, CONSTRAINT fk_player_elo_player__id FOREIGN KEY
(
    player
) REFERENCES player
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT, CONSTRAINT fk_player_elo_match_id__id FOREIGN KEY
(
    match_id
) REFERENCES "match"
(
    id
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT);

CREATE TABLE IF NOT EXISTS player_stats
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
) NOT NULL, updated_date TIMESTAMP NOT NULL, player_id INT NOT NULL, elo REAL NOT NULL, played INT NOT NULL, wins INT NOT NULL, losses INT NOT NULL, goals_for INT NOT NULL, goals_against INT NOT NULL, elo_change REAL NOT NULL, CONSTRAINT fk_player_stats_player_id__id FOREIGN KEY
(
    player_id
) REFERENCES player
(
    id
) ON DELETE RESTRICT
  ON UPDATE RESTRICT);
