package org.example.ui;

import org.example.domain.*;
import org.example.service.EventService;
import org.example.service.FlockService;
import org.example.service.FriendshipService;
import org.example.service.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for interacting with the social network application.
 * Provides menu-driven operations for managing users, friendships, and social network analytics.
 */
public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final FlockService flockService;
    private final EventService eventService;

    private static Console instance;

    private Console(UserService userService, FriendshipService friendshipService, FlockService flockService, EventService eventService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.flockService = flockService;
        this.eventService = eventService;
    }

    public static Console getInstance(UserService userService, FriendshipService friendshipService, FlockService flockService, EventService eventService) {
        if (instance == null) {
            instance = new Console(userService, friendshipService, flockService, eventService);
        }
        return instance;
    }

    /**
     * Displays the main menu of the application.
     */
    public void menu() {
        System.out.println("===== MENU =====");
        System.out.println("1. Add User");
        System.out.println("2. Remove User");
        System.out.println("3. List Users");
        System.out.println("4. Add Friendship");
        System.out.println("5. Remove Friendship");
        System.out.println("6. List Friendships");
        System.out.println("7. Show The Number Of Communities");
        System.out.println("8. Show The Most Sociable Community");
        System.out.println("9. Flock Menu");
        System.out.println("10. Event Menu");
        System.out.println("0. Exit");
        System.out.println("================");
        System.out.print("Choose an option: ");
    }

    /**
     * Displays the flock menu options to the console.
     */
    public void flockMenu() {
        System.out.println("===== FLOCK MENU =====");
        System.out.println("1. Add Flock");
        System.out.println("2. Remove Flock");
        System.out.println("3. List Flocks");
        System.out.println("4. Add Duck To Flock");
        System.out.println("5. Remove Duck From Flock");
        System.out.println("0. Back to Main Menu");
        System.out.println("====================");
        System.out.print("Choose an option: ");
    }

    /**
     * Displays the event menu options to the console.
     */
    public void eventMenu() {
        System.out.println("===== EVENT MENU =====");
        System.out.println("1. Add Event");
        System.out.println("2. Remove Event");
        System.out.println("3. List Events");
        System.out.println("4. Start Race");
        System.out.println("5. Add Spectator To Event");
        System.out.println("6. Remove Spectator From Event");
        System.out.println("7. Add Participant To RaceEvent");
        System.out.println("8. Remove Participant From RaceEvent");
        System.out.println("9. Add Lane To RaceEvent");
        System.out.println("10. Remove Lane From RaceEvent");
        System.out.println("11. Show Lanes");
        System.out.println("0. Back to Main Menu");
        System.out.println("====================");
        System.out.print("Choose an option: ");
    }

    /**
     * Starts the interactive console loop, allowing the user to select menu options
     * and perform corresponding actions.
     */
    public void run() {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        while (run) {
            menu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addUser();
                    break;
                case "2":
                    removeUser();
                    break;
                case "3":
                    listUsers();
                    break;
                case "4":
                    addFriendship();
                    break;
                case "5":
                    removeFriendship();
                    break;
                case "6":
                    listFriendships();
                    break;
                case "7":
                    showNumberOfCommunities();
                    break;
                case "8":
                    showTheMostSociableCommunity();
                    break;
                case "9":
                    boolean flockMenuRun = true;
                    Scanner flockScanner = new Scanner(System.in);
                    while (flockMenuRun) {
                        flockMenu();
                        String eventChoice = flockScanner.nextLine();
                        switch (eventChoice) {
                            case "1":
                                addFlock();
                                break;
                            case "2":
                                removeFlock();
                                break;
                            case "3":
                                listFlocks();
                                break;
                            case "4":
                                addDuckToFlock();
                                break;
                            case "5":
                                removeDuckFromFlock();
                                break;
                            case "0":
                                flockMenuRun = false;
                        }
                    }
                    break;
                case "10":
                    boolean eventMenuRun = true;
                    Scanner eventScanner = new Scanner(System.in);
                    while (eventMenuRun) {
                        eventMenu();
                        String eventChoice = eventScanner.nextLine();
                        switch (eventChoice) {
                            case "1":
                                addEvent();
                                break;
                            case "2":
                                removeEvent();
                                break;
                            case "3":
                                listEvents();
                                break;
                            case "4":
                                startEvent();
                                break;
                            case "5":
                                addSpectatorToEvent();
                                break;
                            case "6":
                                removeSpectatorFromEvent();
                                break;
                            case "7":
                                addParticipantToEvent();
                                break;
                            case "8":
                                removeParticipantFromEvent();
                                break;
                            case "9":
                                addLaneToEvent();
                                break;
                            case "10":
                                removeLaneFromEvent();
                                break;
                            case "11":
                                showLaneDetails();
                                break;
                            case "0":
                                eventMenuRun = false;
                        }
                    }
                    break;
                case "0":
                    run = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    /**
     * Prompts the user to input information and adds a new Person or Duck.
     * All exceptions are caught and printed to the console.
     */
    public void addUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select user type:");
        System.out.println("1. Person");
        System.out.println("2. Duck");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter name: ");
                String name = scanner.nextLine();

                System.out.print("Enter birthdate (yyyy-MM-dd): ");
                String birthdate = scanner.nextLine();

                System.out.print("Enter occupation: ");
                String occupation = scanner.nextLine();

                try {
                    userService.add("Person", username, email, password, surname, name, birthdate, occupation);
                    System.out.println("Person added successfully!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                System.out.print("Enter duck type: ");
                String duckType = scanner.nextLine();

                System.out.print("Enter duck speed: ");
                String duckSpeed = scanner.nextLine();

                System.out.print("Enter duck resistance: ");
                String duckResistance = scanner.nextLine();

                try {
                    userService.add("Duck", username, email, password, duckType, duckSpeed, duckResistance);
                    System.out.println("Duck added successfully!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Prompts the user for a user ID and removes the corresponding user
     * and all their friendships.
     * All exceptions are caught and printed to the console.
     */
    public void removeUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the user you want to remove: ");
        String id = scanner.nextLine();

        try {
            userService.remove(id);
            eventService.removeUserFromAllEvents(id);
            System.out.println("User removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all users in the system.
     */
    public void listUsers() {
        System.out.print("========== All users ==========\n");
        Iterable<User> users = userService.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("===============================");
    }

    /**
     * Prompts for two user IDs and creates a friendship between them.
     * All exceptions are caught and printed to the console.
     */
    public void addFriendship() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the first user: ");
        String firstId = scanner.nextLine();

        System.out.print("Enter the ID of the second user: ");
        String secondId = scanner.nextLine();

        try {
            friendshipService.add(firstId, secondId);
            System.out.println("Friendship added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prompts for a friendship ID and removes the corresponding friendship.
     * All exceptions are caught and printed to the console.
     */
    public void removeFriendship() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the friendship you want to remove: ");
        String id = scanner.nextLine();

        try {
            friendshipService.remove(id);
            System.out.println("Friendship removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all friendships in the system.
     */
    public void listFriendships() {
        System.out.print("======== All friendships ========\n");
        Iterable<Friendship> friendships = friendshipService.findAll();
        for (Friendship friendship : friendships) {
            System.out.println(friendship);
        }
        System.out.println("===============================");
    }

    /**
     * Displays the number of communities in the social network.
     */
    public void showNumberOfCommunities() {
        int numberOfCommunities = friendshipService.getNumberOfCommunities();
        System.out.println("The number of communities is: " + numberOfCommunities);
    }

    /**
     * Displays the most sociable community, i.e., the community with the largest diameter.
     * If there is no such community, prints a message accordingly.
     */
    public void showTheMostSociableCommunity() {
        List<Long> mostSociableCommunity = friendshipService.getMostSociableCommunity();
        if (mostSociableCommunity.isEmpty()) {
            System.out.println("There is no sociable community");
            return;
        }

        System.out.println("The most sociable community contains the following users: ");

        for (Long userId : mostSociableCommunity) {
            User user = userService.findById(userId.toString());
            System.out.println(user);
        }
    }

    /**
     * Adds a new flock with the given name and type.
     */
    public void addFlock() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the flock: ");
        String flockName = scanner.nextLine();

        System.out.print("Enter flock type: ");
        String flockType = scanner.nextLine();

        try {
            flockService.add(flockName, flockType);
            System.out.println("Flock added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a flock by ID.
     */
    public void removeFlock() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the flock you want to remove: ");
        String flockId = scanner.nextLine();

        try {
            flockService.remove(flockId);
            System.out.println("Flock removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lists all flocks in the system.
     */
    public void listFlocks() {
        System.out.print("========== All flocks ==========\n");
        Iterable<Flock> flocks = flockService.findAll();
        for (Flock flock : flocks) {
            System.out.println(flock);
        }
        System.out.println("===============================");
    }

    /**
     * Adds a duck to a specified flock.
     */
    public void addDuckToFlock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the flock: ");
        String flockId = scanner.nextLine();

        System.out.print("Enter the ID of the duck you want to add to the flock: ");
        String duckId = scanner.nextLine();

        try {
            flockService.addDuckToFlock(flockId, duckId);
            System.out.println("Duck added to flock successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a duck from a specified flock.
     */
    public void removeDuckFromFlock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the flock: ");
        String flockId = scanner.nextLine();

        System.out.print("Enter the ID of the duck you want to remove: ");
        String duckId = scanner.nextLine();

        try {
            flockService.removeDuckFromFlock(flockId, duckId);
            System.out.println("Duck removed from flock successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a new event with type and name.
     */
    public void addEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event type: ");
        String type = scanner.nextLine();
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        try {
            eventService.add(type, name);
            System.out.println("Event added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes an event by ID.
     */
    public void removeEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String id = scanner.nextLine();
        try {
            eventService.remove(id);
            System.out.println("Event removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lists all events.
     */
    public void listEvents() {
        System.out.print("========== All events ==========\n");
        Iterable<Event> events = eventService.findAll();
        for (Event event : events) {
            System.out.println(event);
        }
        System.out.println("==============================");
    }

    /**
     * Starts a race event and displays results.
     */
    public void startEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String id = scanner.nextLine();
        try {
            List<DuckResult> results = eventService.start(id);

            double total = 0;

            System.out.println("=== Race Results ===");
            for (int i = 0; i < results.size(); i++) {
                DuckResult dr = results.get(i);
                System.out.println(
                        "Duck " + dr.getDuck().getId() +
                                " on lane " + (i + 1) +
                                ": t = " + String.format("%.3f", dr.getTime()) + " s"
                );

                total = Math.max(total, dr.getTime());
            }

            System.out.println("Total race duration = " +
                    String.format("%.3f", total) + " s");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a spectator to a specified event.
     */
    public void addSpectatorToEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        try {
            eventService.addSpectatorToEvent(eventId, userId);
            System.out.println("Spectator added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a spectator from a specified event.
     */
    public void removeSpectatorFromEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        try {
            eventService.removeSpectatorFromEvent(eventId, userId);
            System.out.println("Spectator removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a participant (duck) to a race event.
     */
    public void addParticipantToEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        try {
            eventService.addParticipantsToEvent(eventId, userId);
            System.out.println("Participant added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a participant from a race event.
     */
    public void removeParticipantFromEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        try {
            eventService.removeParticipantsFromEvent(eventId, userId);
            System.out.println("Participant removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a lane to a race event.
     */
    public void addLaneToEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter lane distance: ");
        String laneValue = scanner.nextLine();
        try {
            eventService.addLaneToRaceEvent(eventId, laneValue);
            System.out.println("Lane added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a lane from a race event.
     */
    public void removeLaneFromEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter lane index: ");
        String indexValue = scanner.nextLine();
        try {
            eventService.removeLaneFromRaceEvent(eventId, indexValue);
            System.out.println("Lane removed successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays the details of lanes associated with a specific race event.
     */
    public void showLaneDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event ID: ");
        String eventId = scanner.nextLine();
        try {
            List<Double> lanes = eventService.getLanes(eventId);
            if (lanes.isEmpty()) {
                System.out.println("No lanes added yet");
            } else {
                RaceEvent race = (RaceEvent) eventService.findById(eventId);
                System.out.println("Lanes for event " + race.getEventName() + ":");
                for (int i = 0; i < lanes.size(); i++) {
                    System.out.println("Lane " + (i + 1) + ": " + lanes.get(i) + " meters");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
