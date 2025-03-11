# NBA Stats Backend

This project is a backend system for managing and retrieving NBA player statistics efficiently using **Spring Boot**, **MySQL**, **Redis**, and **scheduled jobs**.

##  Features
- **Create Player API**: Inserts player data into MySQL, creates teams if they don’t exist, and logs player statistics.
- **Get Player Stats API**: Fetches average statistics per player and season from Redis.
- **Get Team Stats API**: Fetches average statistics per team and season from Redis.
- **Database Triggers**: Logs player and season changes in `change_log` when new game statistics are inserted.
- **Scheduled Job (Every 1 Minute)**: Processes new records in `change_log`, calculates averages, and updates stats in Redis.

---

##  System Architecture

![image](https://github.com/user-attachments/assets/0044a167-2e52-4e0d-8980-db2a62055fa7)

---

##  How to Run the Project

### **1. Clone the Repository**
```sh
git clone https://github.com/your-repo/nba-stats.git
cd nba-stats
```

### **2. Set Up MySQL Database**
- Ensure MySQL is running (use Docker or local installation).
- Create the database:
```sql
CREATE DATABASE nba_stats;
```
- Update `application.properties` with your MySQL credentials.

### **3. Run MySQL in Docker (Optional)**
```sh
docker run --name nba-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=nba_stats -p 3306:3306 -d mysql:latest
```

### **4. Start Redis (Optional, using Docker)**
```sh
docker run --name nba-redis -p 6379:6379 -d redis:latest
```

### **5. Build & Run the Application**
```sh
./gradlew bootRun
```

### **6. Running Tests**
```sh
./gradlew test
```

---

## 🛠 API Endpoints

### **1. Create Player**
**POST** `/players`
```json
{
  "id_number": "12345",
  "name": "LeBron James",
  "team_name": "Lakers",
  "game_stats": {
    "game_date": "2024-03-10",
    "points": 30,
    "rebounds": 8,
    "assists": 10,
    "steals": 2,
    "blocks": 1,
    "fouls": 3,
    "turnovers": 4,
    "minutes_played": 35
  }
}
```

### **2. Get Player Stats**
**GET** `/players/{player_id}/stats`

### **3. Get Team Stats**
**GET** `/teams/{team_id}/stats`

---

## Database Schema
_

---

## Background Job & Triggers
- **Trigger on `game_stats` Insert**: Logs player ID and Season year in `change_log`.
- **Scheduled Job (Every Minute)**:
    1. Checks `change_log` for new records.
    2. Calculates **average player stats** and **average team stats**.
    3. Updates Redis with fresh stats.

---


