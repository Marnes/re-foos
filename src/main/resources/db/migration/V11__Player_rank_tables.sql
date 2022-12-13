CREATE TABLE IF NOT EXISTS player_rank
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player_id    INT                                   NOT NULL,
    "rank"       INT                                   NOT NULL,
    CONSTRAINT fk_player_rank_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS match_player_rank_snapshot
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    player_id    INT                                   NOT NULL,
    "rank"       INT                                   NOT NULL,
    match_id     INT                                   NOT NULL,
    CONSTRAINT fk_match_player_rank_snapshot_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_match_player_rank_snapshot_match_id__id FOREIGN KEY (match_id) REFERENCES "match" (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE match_player_rank_snapshot
    ADD CONSTRAINT match_player_rank_unique_idx UNIQUE (match_id, player_id);
