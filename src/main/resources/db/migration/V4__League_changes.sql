CREATE TABLE IF NOT EXISTS league_config
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    league_id    INT                                   NOT NULL,
    starting_elo REAL                                  NOT NULL,
    CONSTRAINT fk_league_config_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE league_config
    ADD CONSTRAINT league_config_unique_idx UNIQUE (league_id);

ALTER TABLE player
    add column league_id INT;

update player
set league_id = 1;

alter table player
    add CONSTRAINT fk_league_config_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

alter table player
    alter column league_id set not null;

ALTER TABLE player
    ADD CONSTRAINT player_user_league_uniq_idx UNIQUE (user_id, league_id);

alter table player drop constraint player_user_uniq_idx;
