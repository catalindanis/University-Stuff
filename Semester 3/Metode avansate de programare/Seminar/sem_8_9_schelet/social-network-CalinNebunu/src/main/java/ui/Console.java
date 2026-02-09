package ui;

import domain.*;
import repository.RepositoryException;
import service.SocialNetworkService;
import utils.paging.Page;
import validation.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static domain.User.ANSI_CYAN;
import static domain.User.ANSI_RESET;

public class Console {

    private SocialNetworkService service;
    private Scanner scanner;

    public Console(SocialNetworkService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            printMenu();
            System.out.print(">>> Enter command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1": uiAddUser(); break;
                case "2": uiUpdateUser(); break; // <-- intreaba Persoana sau Duck
                case "3": uiAddFriend(); break;
                case "4": uiRemoveUser(); break;
                case "5": uiRemoveFriend(); break;
                case "6": uiShowNumberOfCommunities(); break;
                case "7": uiShowMostSociableCommunity(); break;
                case "8": uiShowAllUsers(); break;
                case "9": uiShowAllFriendsOfUser(); break;

                case "10": uiCreateCard(); break;
                case "11": uiAddDuckToCard(); break;
                case "12": uiRemoveDuckFromCard(); break;
                case "13": uiDeleteCard(); break;
                case "14": uiShowAllCards(); break;

                case "15": uiCreateRaceEvent(); break;
                case "16": uiSubscribeToEvent(); break;
                case "17": uiUnsubscribeFromEvent(); break;
                case "18": uiTriggerEvent(); break;

                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }

    private static void printMenu() {
        System.out.println(ANSI_CYAN + "\n--- DuckSocialNetwork Menu ---" + ANSI_RESET);
        System.out.println("1. Add User (Person/Duck)");
        System.out.println("2. Update User (Person/Duck)");
        System.out.println("3. Add Friend");
        System.out.println("4. Remove User");
        System.out.println("5. Remove Friend");
        System.out.println("6. Show number of communities");
        System.out.println("7. Show most sociable community");
        System.out.println("8. Show all Users");
        System.out.println("9. Show all friends of user");

        System.out.println(ANSI_CYAN + "--- Card Management ---" + ANSI_RESET);
        System.out.println("10. Create Card (Flock)");
        System.out.println("11. Add Duck to Card");
        System.out.println("12. Remove Duck from Card");
        System.out.println("13. Delete Card");
        System.out.println("14. Show all Cards");

        System.out.println(ANSI_CYAN + "--- Event Management ---" + ANSI_RESET);
        System.out.println("15. Create Race Event");
        System.out.println("16. Subscribe to Event");
        System.out.println("17. Unsubscribe from Event");
        System.out.println("18. Trigger Event");

        System.out.println("0. Exit");
    }


    // ----- Social Network -----

    private void uiAddUser() {
        try {
            System.out.print("Type (Person/Duck): ");
            String tip = scanner.nextLine();

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (tip.equalsIgnoreCase("Person")) {
                System.out.print("First Name: ");
                String nume = scanner.nextLine();

                System.out.print("Last Name: ");
                String prenume = scanner.nextLine();

                System.out.print("Date of birth (YYYY-MM-DD): ");
                String dataString = scanner.nextLine();

                LocalDate dataNasterii;
                try {
                    dataNasterii = LocalDate.parse(dataString);
                } catch (DateTimeParseException e) {
                    System.out.println("[INPUT ERROR]: Invalid date format. Use YYYY-MM-DD.");
                    return;
                }

                System.out.print("Occupation: ");
                String ocupatie = scanner.nextLine();

                System.out.print("Empathy level (0-10): ");
                Integer nivelEmpatie = scanner.nextInt();
                scanner.nextLine(); // Consuma newline-ul ramas

                service.addUserPersoana(username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);

            } else if (tip.equalsIgnoreCase("Duck")) {

                System.out.print("Duck type (Swimming, Flying, Hybrid): ");
                String duckType = scanner.nextLine(); // Citim String-ul

                System.out.print("Speed: ");
                Double viteza = scanner.nextDouble();

                System.out.print("Stamina: ");
                Double rezistenta = scanner.nextDouble();
                scanner.nextLine(); // Consuma newline-ul ramas

                System.out.print("(0 = niciun card) Card ID: ");
                String cardInput = scanner.nextLine();
                Long card = null;
                try {
                    Long cardId = Long.parseLong(cardInput);
                    if (cardId != 0) {
                        card = cardId;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[INPUT ERROR]: Invalid card ID, ignoring.");
                }

                service.addUserDuck(username, email, password, duckType.toUpperCase(), viteza, rezistenta, card);

            } else {
                System.out.println("Unknown type!");
                return;
            }

            System.out.println("User added successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[" + e.getClass().getSimpleName() + " ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: Invalid numeric input.");
            scanner.nextLine(); // Curatam buffer-ul
        } catch (DateTimeParseException e) {
            System.out.println("[INPUT ERROR]: Invalid date format. Use YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println("[INPUT ERROR]: Invalid duck type.");
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiUpdateUser() {
        System.out.print("Enter User ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // clear buffer

        User user = service.findOneUser(id);
        if (user instanceof Persoana) {
            uiUpdatePerson(id); // metoda de update pentru Persoana
        } else if (user instanceof Duck) {
            uiUpdateDuck(id); // metoda de update pentru Duck
        } else {
            System.out.println("User not found or unknown type.");
        }
    }

    private void uiUpdatePerson(Long id) {
        try {
            User user = service.findOneUser(id);
            if (!(user instanceof Persoana)) {
                System.out.println("User with ID " + id + " is not a Person.");
                return;
            }
            Persoana persoana = (Persoana) user;

            System.out.print("New Username (current: " + persoana.getUsername() + ", enter '.' to keep): ");
            String username = scanner.nextLine();
            if (!username.equals(".")) persoana.setUsername(username);

            System.out.print("New Email (current: " + persoana.getEmail() + ", enter '.' to keep): ");
            String email = scanner.nextLine();
            if (!email.equals(".")) persoana.setEmail(email);

            System.out.print("New Password (enter '.' to keep): ");
            String password = scanner.nextLine();
            if (!password.equals(".")) persoana.setPassword(password);

            System.out.print("New First Name (current: " + persoana.getNume() + ", enter '.' to keep): ");
            String nume = scanner.nextLine();
            if (!nume.equals(".")) persoana.setNume(nume);

            System.out.print("New Last Name (current: " + persoana.getPrenume() + ", enter '.' to keep): ");
            String prenume = scanner.nextLine();
            if (!prenume.equals(".")) persoana.setPrenume(prenume);

            System.out.print("New Date of birth (YYYY-MM-DD, current: " + persoana.getDataNasterii() + ", enter '.' to keep): ");
            String dob = scanner.nextLine();
            if (!dob.equals(".")) persoana.setDataNasterii(LocalDate.parse(dob));

            System.out.print("New Occupation (current: " + persoana.getOcupatie() + ", enter '.' to keep): ");
            String ocupatie = scanner.nextLine();
            if (!ocupatie.equals(".")) persoana.setOcupatie(ocupatie);

            System.out.print("New Empathy level (0-10, current: " + persoana.getNivelEmpatie() + ", enter '.' to keep): ");
            String nivel = scanner.nextLine();
            if (!nivel.equals(".")) persoana.setNivelEmpatie(Integer.parseInt(nivel));

            service.updateUserPersoana(persoana);
            System.out.println("Person updated successfully!");
        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiUpdateDuck(Long id) {
        try {
            User user = service.findOneUser(id);
            if (!(user instanceof Duck)) {
                System.out.println("User with ID " + id + " is not a Duck.");
                return;
            }
            Duck duck = (Duck) user;

            System.out.print("New Username (current: " + duck.getUsername() + ", enter '.' to keep): ");
            String username = scanner.nextLine();
            if (!username.equals(".")) duck.setUsername(username);

            System.out.print("New Email (current: " + duck.getEmail() + ", enter '.' to keep): ");
            String email = scanner.nextLine();
            if (!email.equals(".")) duck.setEmail(email);

            System.out.print("New Password (enter '.' to keep): ");
            String password = scanner.nextLine();
            if (!password.equals(".")) duck.setPassword(password);

            System.out.print("New Speed (current: " + duck.getViteza() + ", enter '.' to keep): ");
            String speed = scanner.nextLine();
            if (!speed.equals(".")) duck.setViteza(Double.parseDouble(speed));

            System.out.print("New Stamina (current: " + duck.getRezistenta() + ", enter '.' to keep): ");
            String stamina = scanner.nextLine();
            if (!stamina.equals(".")) duck.setRezistenta(Double.parseDouble(stamina));

            if (duck.getCardId() == null) {
                System.out.print("New Duck Type (current: " + duck.getDuckType() + ", enter '.' to keep): ");
                String type = scanner.nextLine();
                if (!type.equals(".")) duck.setDuckType(type);
            } else {
                System.out.println("Duck is attached to a card. Duck Type cannot be changed.");
            }

            service.updateUserDuck(duck);
            System.out.println("Duck updated successfully!");
        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }


    private void uiAddFriend() {
        try {
            System.out.print("Enter first user ID: ");
            Long id1 = scanner.nextLong();
            System.out.print("Enter second user ID: ");
            Long id2 = scanner.nextLong();

            scanner.nextLine(); // Consuma newline-ul ramas

            service.addFriend(id1, id2);

            System.out.println("Friendship added successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter numbers (IDs).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiRemoveUser() {
        try {
            System.out.print("Enter the ID of the user to delete: ");
            Long id = scanner.nextLong();

            scanner.nextLine(); // Consuma newline-ul ramas

            service.removeUser(id);

            System.out.println("User deleted successfully! (and associated friendships)");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter a number (ID).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiRemoveFriend() {
        try {
            System.out.print("Enter first user ID: ");
            Long id1 = scanner.nextLong();

            System.out.print("Enter second user ID: ");
            Long id2 = scanner.nextLong();

            scanner.nextLine(); // Consuma newline-ul ramas

            service.removeFriend(id1, id2);

            System.out.println("Friendship removed successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter numbers (IDs).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiShowNumberOfCommunities() {
        try {
            int count = service.getNumberOfCommunities();
            System.out.println("Total number of communities in the network: " + count);
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiShowMostSociableCommunity() {
        try {
            Set<Long> community = service.getMostSociableCommunity();

            if (community.isEmpty()) {
                System.out.println("The network is empty or no communities were found.");
            } else {
                System.out.println("The most sociable community (with the largest diameter) consists of users with IDs:");
                System.out.println(community);
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiShowAllUsers() {
        try {
            for (User user : service.findAllUsers()) {
                System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiShowAllFriendsOfUser() {
        try {
            System.out.print("Enter User ID: ");
            Long id = scanner.nextLong();
            scanner.nextLine();
            Set<Long> friends = service.findAllFriendsOfUser(id);
            System.out.println("Friends of user " + id + ": " + friends);
        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }


    // ----- Card -----

    private void uiCreateCard() {
        try {
            System.out.print("Enter Card name: ");
            String numeCard = scanner.nextLine();

            System.out.print("Enter Card type (Swimming, Flying, Hybrid): ");
            String tipCard = scanner.nextLine();

            service.createCard(numeCard, tipCard);
            System.out.println("Card created successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiAddDuckToCard() {
        try {
            System.out.print("Enter Duck ID to add: ");
            Long duckId = scanner.nextLong();

            System.out.print("Enter Card ID to add to: ");
            Long cardId = scanner.nextLong();
            scanner.nextLine(); // Consuma newline

            service.addDuckToCard(duckId, cardId);
            System.out.println("Duck added to Card successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter numbers (IDs).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiRemoveDuckFromCard() {
        try {
            System.out.print("Enter Duck ID to remove: ");
            Long duckId = scanner.nextLong();

            scanner.nextLine(); // Consuma newline

            service.removeDuckFromCard(duckId);
            System.out.println("Duck removed from Card successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter numbers (IDs).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiDeleteCard() {
        try {
            System.out.print("Enter Card ID to delete: ");
            Long cardId = scanner.nextLong();
            scanner.nextLine(); // Consuma newline

            service.deleteCard(cardId);
            System.out.println("Card deleted successfully! (Members are now flock-less)");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter a number (ID).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiShowAllCards() {
        int pageSize = 2;  // câte carduri pe pagină vrei
        int currentPage = 0;

        while (true) {
            try {
                Page<Card> page = service.getCardsOnPage(currentPage, pageSize);

                System.out.println("\n=== Cards (page " + currentPage + ") ===");
                for (Card c : page.getElementsOnPage()) {
                    System.out.println(c);
                }

                int totalPages = (page.getTotalNumberOfElements() + pageSize - 1) / pageSize;
                System.out.println("Page " + currentPage + " of " + (totalPages - 1));

                System.out.println("[n] Next  |  [p] Previous  |  [q] Quit");
                System.out.print(">>> ");

                String cmd = scanner.nextLine().trim().toLowerCase();

                switch (cmd) {
                    case "n":
                        if (currentPage < totalPages - 1) currentPage++;
                        else System.out.println("Already on last page!");
                        break;

                    case "p":
                        if (currentPage > 0) currentPage--;
                        else System.out.println("Already on first page!");
                        break;

                    case "q":
                        return;

                    default:
                        System.out.println("Invalid command.");
                }

            } catch (Exception e) {
                System.out.println("[ERROR]: " + e.getMessage());
                return;
            }
        }
    }



    // ----- Event -----

    private void uiCreateRaceEvent() {
        try {
            System.out.print("Enter event description: ");
            String description = scanner.nextLine();

            List<Double> distantaBalize = new ArrayList<>();
            System.out.print("Enter number of buoys (M): ");
            int M = scanner.nextInt();
            scanner.nextLine(); // Consuma newline

            System.out.println("Enter the distance for each buoy (must be in increasing order):");
            for (int i = 0; i < M; i++) {
                System.out.print("Distance for buoy " + (i + 1) + ": ");
                double dist = scanner.nextDouble();
                distantaBalize.add(dist);
            }
            scanner.nextLine(); // Consuma newline

            // Apelam metoda actualizata
            service.createRaceEvent(description, distantaBalize);
            System.out.println("Race Event created successfully!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: Invalid numeric input.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiSubscribeToEvent() {
        try {
            System.out.print("Enter your User ID: ");
            Long userId = scanner.nextLong();

            System.out.print("Enter the Event ID to subscribe to: ");
            Long eventId = scanner.nextLong();
            scanner.nextLine(); // Consuma newline

            service.subscribeUserToEvent(userId, eventId);
            System.out.println("Successfully subscribed to event!");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter numbers (IDs).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiUnsubscribeFromEvent() {
        try {
            System.out.print("Enter User ID: ");
            Long userId = scanner.nextLong();
            System.out.print("Enter Event ID: ");
            Long eventId = scanner.nextLong();
            scanner.nextLine();
            service.unsubscribeUserFromEvent(userId, eventId);
            System.out.println("User unsubscribed successfully from the event.");
        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private void uiTriggerEvent() {
        try {
            System.out.print("Enter the Event ID to trigger: ");
            Long eventId = scanner.nextLong();
            scanner.nextLine(); // Consuma newline

            System.out.println("--- Triggering Event " + eventId + " ---");
            service.triggerEvent(eventId);
            System.out.println("--- Event Triggered ---");

        } catch (ValidationException | RepositoryException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("[INPUT ERROR]: You must enter a number (ID).");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("[SYSTEM ERROR]: " + e.getMessage());
        }
    }

}