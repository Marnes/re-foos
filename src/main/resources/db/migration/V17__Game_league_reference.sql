ALTER TABLE game
    ADD COLUMN league_id        INT,
    ADD COLUMN league_season_id INT;

UPDATE game
SET league_id        = (select league_id from match where match.id = game.match_id),
    league_season_id = (select id
                        from league_season ls
                        where ls.league_id = (select league_id from match where match.id = game.match_id));

ALTER TABLE game
    ADD CONSTRAINT fk_game_league_id__id FOREIGN KEY (league_id) REFERENCES league (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_game_league_season_id__id FOREIGN KEY (league_season_id) REFERENCES league_season (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE game
    ALTER COLUMN league_id SET NOT NULL,
    ALTER COLUMN league_season_id SET NOT NULL;
