CREATE TABLE IF NOT EXISTS match_winners
(
    match_stats_id INT,
    player_id      INT,
    CONSTRAINT pk_MatchWinners PRIMARY KEY (match_stats_id, player_id),
    CONSTRAINT fk_matchwinners_match_stats_id__id FOREIGN KEY (match_stats_id) REFERENCES match_stats (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_matchwinners_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS match_losers
(
    match_stats_id INT,
    player_id      INT,
    CONSTRAINT pk_MatchLosers PRIMARY KEY (match_stats_id, player_id),
    CONSTRAINT fk_matchlosers_match_stats_id__id FOREIGN KEY (match_stats_id) REFERENCES match_stats (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_matchlosers_player_id__id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO match_winners
SELECT id, winner FROM match_stats
WHERE winner IS NOT NULL;

INSERT INTO match_losers
SELECT id, loser FROM match_stats
WHERE loser IS NOT NULL;

ALTER TABLE match_stats DROP COLUMN winner;
ALTER TABLE match_stats DROP COLUMN loser;
