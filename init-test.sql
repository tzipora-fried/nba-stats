CREATE
DATABASE IF NOT EXISTS nba_stats;

USE nba_stats;

CREATE TABLE IF NOT EXISTS teams
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS  players
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    id_number VARCHAR(20) UNIQUE NOT NULL,
    name      VARCHAR(100)       NOT NULL,
    team_id   INT                NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE,
    INDEX     idx_team (team_id)
);

CREATE TABLE IF NOT EXISTS game_stats
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    player_id      INT       NOT NULL,
    points         INT       NOT NULL DEFAULT 0,
    rebounds       INT       NOT NULL DEFAULT 0,
    assists        INT       NOT NULL DEFAULT 0,
    steals         INT       NOT NULL DEFAULT 0,
    blocks         INT       NOT NULL DEFAULT 0,
    fouls          INT       NOT NULL DEFAULT 0 CHECK (fouls <= 6),
    turnovers      INT       NOT NULL DEFAULT 0,
    minutes_played FLOAT     NOT NULL DEFAULT 0 CHECK (minutes_played BETWEEN 0 AND 48.0),
    game_date      TIMESTAMP NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE,
    INDEX          idx_player (player_id)
);

CREATE TABLE IF NOT EXISTS  change_log
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    player_id   INT NOT NULL,
    season_year INT NOT NULL,
    change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);










