# DuckSocialNetwork – Lab 4

---

## Features

- **Add/Remove Flock (Cârd)**  
  Adding and removing flocks of ducks with a common goal (e.g., `SwimMasters`, `SkyFlyers`).  
  Attributes:
    - `id` – unique identifier  
    - `numeCârd` – name of the flock  
    - `membri: List<Duck>` – list of ducks  
  Methods:
    - `getPerformantaMedie()` – calculates average speed and stamina of members  

- **Add/Remove Event**  
  Adding and removing events in the network.  
  Attributes:
    - `subscribers: List<User>` – users observing the event  
  Methods:
    - `subscribe(User u)`  
    - `unsubscribe(User u)`  
    - `notifySubscribers()` (Observer Pattern)  

- **RaceEvent**  
  Special event for swimming ducks.  
  Features:
    - Automatically selects `M` ducks according to the “Natație” rules  
    - Notifies subscribed users  

---

## Additional Features

- **Layered Architecture**  
- **DDD (Domain Driven Design)**  
- **Documentation – JavaDoc:** (Tools → Generate JavaDoc in IntelliJ IDEA)  
- **Data Persistence –** in memory or files  
- **Data Validation –** using Strategy Pattern  
- **Custom Exception Classes**  
- **Console-Based User Interface (minimalist)**  
