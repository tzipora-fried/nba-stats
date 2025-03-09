-- יצירת בסיס הנתונים (ליתר ביטחון)
CREATE
DATABASE IF NOT EXISTS nba_stats;

USE nba_stats;

-- טבלת קבוצות
CREATE TABLE IF NOT EXISTS teams
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE -- שם הקבוצה ייחודי
);

-- טבלת שחקנים
CREATE TABLE IF NOT EXISTS  players
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    id_number VARCHAR(20) UNIQUE NOT NULL, -- תעודת זהות ייחודית
    name      VARCHAR(100)       NOT NULL,
    team_id   INT                NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE,
    INDEX     idx_team (team_id)           -- אינדקס על team_id לחיפושים מהירים
);

-- טבלת סטטיסטיקות משחקים
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
    game_date      TIMESTAMP NOT NULL, -- הוספתי את תאריך המשחק כאן
    FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE,
    INDEX          idx_player (player_id)
);

CREATE TABLE IF NOT EXISTS  change_log
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    entity_type ENUM('player', 'team') NOT NULL,
    entity_id   INT NOT NULL,
    change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- שינוי delimiter כדי לתמוך בטריגרים
DELIMITER $$

-- טריגר לעדכון סטטיסטיקות שחקן
CREATE TRIGGER after_player_stats_update
    AFTER INSERT ON game_stats
    FOR EACH ROW
BEGIN
    INSERT INTO change_log (entity_type, entity_id)
    VALUES ('player', NEW.player_id);
    END$$

    -- טריגר לעדכון סטטיסטיקות קבוצה
    CREATE TRIGGER after_team_stats_update
        AFTER INSERT ON game_stats
        FOR EACH ROW
    BEGIN
        -- משתנה שמכיל את ה-team_id
        DECLARE team_id INT DEFAULT NULL;

    -- שליפת ה-team_id של השחקן
        SELECT team_id INTO team_id FROM players WHERE id = NEW.player_id LIMIT 1;

        -- הוספת עדכון רק אם נמצא team_id תקין
        IF team_id IS NOT NULL THEN
        INSERT INTO change_log (entity_type, entity_id)
        VALUES ('team', team_id);
    END IF;
    END$$

-- החזרת ה-DELIMITER הרגיל
    DELIMITER ;




