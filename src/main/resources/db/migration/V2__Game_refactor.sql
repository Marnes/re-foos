alter table game
    rename to game_old;

alter sequence game_id_seq rename to game_old_id_seq;

alter table "user"
    add column avatar varchar(255);

alter table player
    drop column avatar;

CREATE TABLE IF NOT EXISTS game
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_game_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS team
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    game_id      INT                                   NOT NULL,
    CONSTRAINT fk_team_game_id__id FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS team_player
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    team_id      INT                                   NOT NULL,
    player_id    INT                                   NOT NULL,
    CONSTRAINT fk_team_player_team_id__id FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_team_player_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE team_player
    ADD CONSTRAINT team_player_unique_idx UNIQUE (team_id, player_id);

CREATE TABLE IF NOT EXISTS team_scores
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    team_id      INT                                   NOT NULL,
    score        INT                                   NOT NULL,
    CONSTRAINT fk_team_scores_team_id__id FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
