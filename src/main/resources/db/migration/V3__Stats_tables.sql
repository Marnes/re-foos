drop table player_elo;
drop table player_stats;

CREATE TABLE IF NOT EXISTS game_player_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player_id    INT                                   NOT NULL,
    elo_change   REAL                                  NOT NULL,
    game_id      INT                                   NOT NULL,
    CONSTRAINT fk_game_player_stats_player__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_player_stats_game_id__id FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE game_player_stats
    ADD CONSTRAINT game_player_stats_unique_idx UNIQUE (game_id, player_id);

CREATE TABLE IF NOT EXISTS game_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    winner       INT,
    loser        INT,
    total_scored INT                                   NOT NULL,
    game_id      INT                                   NOT NULL,
    CONSTRAINT fk_game_stats_winner__id FOREIGN KEY (winner) REFERENCES team (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_stats_loser__id FOREIGN KEY (loser) REFERENCES team (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_game_stats_game_id__id FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE game_stats
    ADD CONSTRAINT game_results_unique_idx UNIQUE (game_id);

CREATE TABLE IF NOT EXISTS match_player_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player_id    INT                                   NOT NULL,
    elo_change   REAL                                  NOT NULL,
    initial_elo  REAL                                  NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_match_player_stats_player__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_player_stats_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE match_player_stats
    ADD CONSTRAINT match_player_stats_unique_idx UNIQUE (match_id, player_id);

CREATE TABLE IF NOT EXISTS match_stats
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    winner       INT,
    loser        INT,
    total_scored INT                                   NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_match_stats_winner__id FOREIGN KEY (winner) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_stats_loser__id FOREIGN KEY (loser) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_stats_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE match_stats
    ADD CONSTRAINT match_results_unique_idx UNIQUE (match_id);

CREATE TABLE IF NOT EXISTS player_stats
(
    id                     SERIAL PRIMARY KEY,
    created_date           TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date           TIMESTAMP                             NOT NULL,
    player_id              INT                                   NOT NULL,
    elo_change             REAL                                  NOT NULL,
    elo                    REAL                                  NOT NULL,
    highest_elo            REAL                                  NOT NULL,
    lowest_elo             REAL                                  NOT NULL,
    played                 INT                                   NOT NULL,
    wins                   INT                                   NOT NULL,
    losses                 INT                                   NOT NULL,
    score_for              INT                                   NOT NULL,
    score_against          INT                                   NOT NULL,
    winning_streak         INT                                   NOT NULL,
    losing_streak          INT                                   NOT NULL,
    longest_winning_streak INT                                   NOT NULL,
    longest_losing_streak  INT                                   NOT NULL,
    CONSTRAINT fk_player_stats_player__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE player_stats
    ADD CONSTRAINT player_stats_unique_idx UNIQUE (player_id);

CREATE TABLE IF NOT EXISTS player_stats_snapshot
(
    id                     SERIAL PRIMARY KEY,
    created_date           TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date           TIMESTAMP                             NOT NULL,
    player_id              INT                                   NOT NULL,
    elo_change             REAL                                  NOT NULL,
    score_for              INT                                   NOT NULL,
    score_against          INT                                   NOT NULL,
    elo                    REAL                                  NOT NULL,
    played                 INT                                   NOT NULL,
    wins                   INT                                   NOT NULL,
    losses                 INT                                   NOT NULL,
    highest_elo            REAL                                  NOT NULL,
    lowest_elo             REAL                                  NOT NULL,
    winning_streak         INT                                   NOT NULL,
    losing_streak          INT                                   NOT NULL,
    longest_winning_streak INT                                   NOT NULL,
    longest_losing_streak  INT                                   NOT NULL,
    match_id               INT                                   NOT NULL,
    CONSTRAINT fk_player_stats_snapshot_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_player_stats_snapshot_match_id__id FOREIGN KEY (match_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE player_stats_snapshot
    ADD CONSTRAINT player_match_stats_snapshot_unique_idx UNIQUE (match_id, player_id);
