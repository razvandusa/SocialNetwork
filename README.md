## Data Persistence and Database Integration

- **Database:** PostgreSQL is used to persist all data in a relational database.  
- **Tables:**  
  - `users` – stores all user accounts (Person and Duck).  
  - `friendships` – stores bidirectional relationships between users.  
  - `flocks` – stores flocks created in the system.  
  - `flock_duck` – links ducks to flocks.  
  - `events` – stores race events.  
  - `event_subscriber` – stores user subscriptions to events.  
  - `lanes` – stores lanes associated with each race event.  
  - `raceevent_participant` – stores participants in race events.  

- **Relationships:**  
  - `users` ↔ `friendships` (many-to-many via friendship links)  
  - `users` ↔ `raceevent_participant` ↔ `events` (many-to-many)  
  - `users` ↔ `event_subscriber` ↔ `events` (many-to-many)  
  - `flocks` ↔ `flock_duck` ↔ `ducks` (many-to-many)  
  - `events` ↔ `lanes` (one-to-many)  

- **Persistence Layer:**  
  - JDBC is used for database communication.  
  - All CRUD operations on users, friendships, flocks, ducks, and events are persisted.  
  - Initial table creation is handled automatically when the application runs.  

---

## Setup Instructions

1. **Install PostgreSQL** and ensure it is running.  
2. **Create Database:**  
   ```sql
   CREATE DATABASE SocialNetwork;
