ALTER TABLE "user"
    ADD COLUMN "admin" BOOLEAN DEFAULT FALSE NOT NULL;

ALTER TABLE league
    ADD COLUMN start_date DATE,
    ADD COLUMN end_date   DATE,
    ADD COLUMN uid        VARCHAR(10);

UPDATE league
set start_date = now(),
    uid        = '1234567';

ALTER TABLE league
    ALTER COLUMN start_date SET NOT NULL,
    ALTER COLUMN uid SET NOT NULL;

ALTER TABLE league
    ADD CONSTRAINT league_unique_uid UNIQUE (uid);

ALTER TABLE league_config
    ADD COLUMN "type"           VARCHAR(255),
    ADD COLUMN games            INT,
    ADD COLUMN teams            INT,
    ADD COLUMN players          INT,
    ADD COLUMN scores_per_team  INT,
    ADD COLUMN players_per_team INT;

UPDATE league_config
set "type"           = 'ROUND_ROBIN',
    games            = 3,
    teams            = 2,
    players          = 4,
    scores_per_team  = 2,
    players_per_team = 2;

ALTER TABLE league_config
    ALTER COLUMN "type" SET NOT NULL,
    ALTER COLUMN games SET NOT NULL,
    ALTER COLUMN teams SET NOT NULL,
    ALTER COLUMN players SET NOT NULL,
    ALTER COLUMN scores_per_team SET NOT NULL,
    ALTER COLUMN players_per_team SET NOT NULL;
