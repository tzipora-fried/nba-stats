# NBA Stats 

This project is a system for managing and retrieving NBA player statistics efficiently using **Spring Boot**, **MySQL**, **Redis**, and **scheduled jobs**.

##  Features
- **Create Player API**: Inserts player data into MySQL, creates teams if they don’t exist, and logs player statistics.
- **Get Player Stats API**: Fetches average statistics per player and season from Redis.
- **Get Team Stats API**: Fetches average statistics per team and season from Redis.
- **Database Triggers**: Logs player and season changes in `change_log` when new game statistics are inserted.
- **Scheduled Job (Every 1 Minute)**: Processes new records in `change_log`, calculates averages, and updates stats in Redis.

---

##  System Architecture

![img.png](img.png)
---

##  How to Run the Project

### **1. Clone the Repository**
```sh
git clone https://github.com/tzipora-fried/nba-stats.git
```
### **2. Navigate into the project directory**
```sh
cd nba-stats
```
### **3. Build the project using Gradle:**
```sh
./gradlew build 
```

### **4. Start the application and required services using Docker Compose**
```sh
docker-compose up --build
```

`
The API will be accessible at:
http://localhost:8080
`
---

## API Endpoints

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
**GET** `/api/stats/player/{playerId}/season/{seasonYear}`

### **3. Get Team Stats**
**GET** `/api/stats/team/{teamId}/season/{seasonYear}`

---

## Database Schema
_

![img_1.png](img_1.png)

---

## Background Job & Triggers
- **Trigger on `game_stats` Insert**: Logs player ID and Season year in `change_log`.
- **Scheduled Job (Every Minute)**:
    1. Checks `change_log` for new records.
    2. Calculates **average player stats** and **average team stats**.
    3. Updates Redis with updated stats.

---

## Future Improvements

Initially, I planned to implement this system using **Debezium** for real-time change data capture (CDC) instead of using database triggers and scheduled jobs.  
However, due to time constraints, I opted for a more straightforward approach with MySQL triggers and scheduled tasks.

A future iteration of this project could replace the **change_log** table and scheduled jobs with **Debezium** to stream database changes directly to **Kafka**, ensuring real-time updates to Redis with improved scalability and reliability.



