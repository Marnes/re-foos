drop table player_elo;
drop table player_stats;

CREATE TABLE IF NOT EXISTS game_player_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player       INT                                   NOT NULL,
    elo_change   REAL                                  NOT NULL,
    game_id      INT                                   NOT NULL,
    CONSTRAINT fk_game_player_stats_player__id FOREIGN KEY (player) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_player_stats_game_id__id FOREIGN KEY (game_id) REFERENCES game_old (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE game_player_stats
    ADD CONSTRAINT game_player_stats_unique_idx UNIQUE (game_id, player);

CREATE TABLE IF NOT EXISTS game_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    winner       INT                                   NOT NULL,
    loser        INT                                   NOT NULL,
    "eloChange"  REAL                                  NOT NULL,
    game_id      INT                                   NOT NULL,
    CONSTRAINT fk_game_stats_winner__id FOREIGN KEY (winner) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_stats_loser__id FOREIGN KEY (loser) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_stats_game_id__id FOREIGN KEY (game_id) REFERENCES game_old (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE game_stats
    ADD CONSTRAINT game_results_unique_idx UNIQUE (game_id);

CREATE TABLE IF NOT EXISTS match_player_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player       INT                                   NOT NULL,
    elo_change   REAL                                  NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_match_player_stats_player__id FOREIGN KEY (player) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_player_stats_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE match_player_stats
    ADD CONSTRAINT match_player_stats_unique_idx UNIQUE (match_id, player);

CREATE TABLE IF NOT EXISTS match_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    winner       INT                                   NOT NULL,
    loser        INT                                   NOT NULL,
    "eloChange"  REAL                                  NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_match_stats_winner__id FOREIGN KEY (winner) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_stats_loser__id FOREIGN KEY (loser) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_stats_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE match_stats
    ADD CONSTRAINT match_results_unique_idx UNIQUE (match_id);

CREATE TABLE IF NOT EXISTS league_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    league_id    INT                                   NOT NULL,
    CONSTRAINT fk_league_stats_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE league_stats
    ADD CONSTRAINT league_stats_unique_idx UNIQUE (league_id);

CREATE TABLE IF NOT EXISTS random_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    league_id    INT                                   NOT NULL,
    player_id    INT                                   NOT NULL,
    amount       REAL                                  NOT NULL,
    "type"       VARCHAR(255)                          NOT NULL,
    CONSTRAINT fk_random_stats_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_random_stats_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS player_stats
(
    id             SERIAL PRIMARY KEY,
    created_date   TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date   TIMESTAMP                             NOT NULL,
    player         INT                                   NOT NULL,
    elo_change     REAL                                  NOT NULL,
    elo            REAL                                  NOT NULL,
    played         INT                                   NOT NULL,
    wins           INT                                   NOT NULL,
    losses         INT                                   NOT NULL,
    score_for      INT                                   NOT NULL,
    score_against  INT                                   NOT NULL,
    winning_streak INT                                   NOT NULL,
    losing_streak  INT                                   NOT NULL,
    CONSTRAINT fk_player_stats_player__id FOREIGN KEY (player) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE player_stats
    ADD CONSTRAINT player_stats_unique_idx UNIQUE (player);
