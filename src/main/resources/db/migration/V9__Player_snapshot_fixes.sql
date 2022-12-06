ALTER TABLE player_stats_snapshot
    rename to match_player_stats_snapshot;

ALTER TABLE match_player_stats_snapshot
    DROP CONSTRAINT fk_player_stats_snapshot_match_id__id;

ALTER TABLE match_player_stats_snapshot
    ADD CONSTRAINT fk_player_stats_snapshot_match_id__id FOREIGN KEY (match_id) REFERENCES match (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

