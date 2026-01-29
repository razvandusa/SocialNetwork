# LuckyLink – Social Network

## Non-Functional Requirements

- **GUI for managing Users (including Duck-related attributes):**
    - Users are displayed in a JavaFX `TableView`.
    - The application allows selecting duck types (`Swimming`, `Flying`, `Flying and Swimming`) from a `ComboBox` and updates the table accordingly.
    - Core flows (login/admin/user interactions) are implemented through JavaFX windows (FXML + CSS).

- **Repository pagination from DB to GUI:**
    - The application implements pagination that allows us to load the data from PostgreSQL into the GUI page by page (not all at once).

---

## Setup Instructions

1. **Install PostgreSQL** and ensure it is running.

2. **Create Database:**
   ```sql
   CREATE DATABASE SocialNetwork;
   ```

3. **Create tables / schema**
    - Run your SQL schema script(s) to create the required tables.
    - Ensure the schema matches what your repositories expect.

4. **Configure DB connection**
    - Set the PostgreSQL connection parameters used by the app:
        - URL: `jdbc:postgresql://localhost:5432/SocialNetwork`
        - Username: `postgres`
        - Password: `your_password`
    - If you keep credentials in code, update them before running.
    - If you use environment variables / config files, set them accordingly.

5. **Run the application**
    - Open the project in IntelliJ IDEA.
    - Let Gradle sync.
    - Run the JavaFX entry point (e.g., `HelloApplication` / your main launcher).

---

## Tech Stack

- **Java:** JDK 25
- **UI:** JavaFX (FXML + CSS)
- **Database:** PostgreSQL
- **Build Tool:** Gradle

---

## Project Structure (high level)

- `src/main/java/org.example/`
    - `controller/` – JavaFX controllers
    - `domain/` – entities / models
    - `exceptions/` – custom exceptions
    - `repository/` – DB repositories (persistence)
    - `service/` – business logic
    - `ui/` – UI utilities / additional UI code
    - `validation/` – validators / strategies
- `src/main/resources/`
    - `fxml/` – UI layouts
    - `css/` – styling

---

## Notes / Troubleshooting

- If the GUI loads but shows no data:
    - confirm the DB is running,
    - confirm tables exist and contain data,
    - confirm connection URL/user/password are correct,
    - check console logs for SQL exceptions.