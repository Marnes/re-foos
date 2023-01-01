CREATE TABLE IF NOT EXISTS league_season
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP) NOT NULL,
    updated_date TIMESTAMP                             NOT NULL,
    created_by   INT                                   NOT NULL,
    league_id    INT                                   NOT NULL,
    season       INT                                   NOT NULL,
    start_date   DATE                                  NOT NULL,
    end_date     DATE                                  NULL,
    CONSTRAINT fk_league_season_created_by__id FOREIGN KEY (created_by) REFERENCES "user" (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_league_season_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE league_season
    ADD CONSTRAINT league_season_unique_idx UNIQUE (league_id, season);

INSERT INTO league_season(created_date, updated_date, created_by, league_id, season, start_date, end_date)
SELECT now(), now(), 1, id, 1, league.start_date, league.end_date
from league;

ALTER TABLE league
    DROP COLUMN start_date;
ALTER TABLE league
    DROP COLUMN end_date;

ALTER TABLE player_stats
    ADD COLUMN league_id        INT,
    ADD COLUMN league_season_id INT;

ALTER TABLE player_rank
    ADD COLUMN league_id        INT,
    ADD COLUMN league_season_id INT;

ALTER TABLE match
    ADD COLUMN league_season_id INT;

UPDATE player_stats
SET league_id        = (select league_id from player where player.id = player_stats.player_id),
    league_season_id = (select id
                        from league_season ls
                        where ls.league_id = (select league_id from player where player.id = player_stats.player_id));
UPDATE player_rank
SET league_id        = (select league_id from player where player.id = player_rank.player_id),
    league_season_id = (select id
                        from league_season ls
                        where ls.league_id = (select league_id from player where player.id = player_rank.player_id));
UPDATE "match"
SET league_season_id = (select id
                        from league_season ls
                        where ls.league_id = "match".league_id);

ALTER TABLE player_stats
    ADD CONSTRAINT fk_player_stats_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_player_stats_league_season_id__id FOREIGN KEY (league_season_id) REFERENCES league_season (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE player_rank
    ADD CONSTRAINT fk_player_rank_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_player_rank_league_season_id__id FOREIGN KEY (league_season_id) REFERENCES league_season (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "match"
    ADD CONSTRAINT fk_match_league_season_id__id FOREIGN KEY (league_season_id) REFERENCES league_season (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE player_rank
    ALTER COLUMN league_id SET NOT NULL,
    ALTER COLUMN league_season_id SET NOT NULL;

ALTER TABLE player_stats
    ALTER COLUMN league_id SET NOT NULL,
    ALTER COLUMN league_season_id SET NOT NULL;

ALTER TABLE "match"
    ALTER COLUMN league_season_id SET NOT NULL;

ALTER TABLE player_stats DROP CONSTRAINT player_stats_unique_idx;

ALTER TABLE player_rank
    ADD CONSTRAINT player_rank_league_season_unique_idx UNIQUE (player_id, league_id, league_season_id);

ALTER TABLE player_stats
    ADD CONSTRAINT player_stats_league_season_unique_idx UNIQUE (player_id, league_id, league_season_id);

