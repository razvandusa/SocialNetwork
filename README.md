# DuckSocialNetwork – Lab 6

## Non-Functional Requirements

- **GUI for the Duck entity:**  
  - Duck users are loaded into a `TableView`.  
  - The application allows selecting duck types (`Flying`, `Swimming`, `Flying and Swimming`) from a `ComboBox` and updates the table accordingly.  

- **Repository pagination from DB to GUI:**  
  - Pagination implementation from the previous lab is preserved, so data is loaded from the database into the graphical interface page by page.  

- **Integration of Lab 3 requirements:**  
  - All functionalities implemented in Lab 3 must be integrated and accessible through the GUI.  

---

## Setup Instructions

1. **Install PostgreSQL** and ensure it is running.  
2. **Create Database:**  
   ```sql
   CREATE DATABASE SocialNetwork;
