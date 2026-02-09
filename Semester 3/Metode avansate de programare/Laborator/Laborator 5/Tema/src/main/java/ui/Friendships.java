package ui;

import exceptions.FriendshipException;
import exceptions.UserException;
import models.FriendshipDTO;
import models.PersonDTO;
import services.FriendshipsService;
import services.UsersService;

public class Friendships extends AbstractUi {
    private static Friendships instance;

    protected Friendships() {}

    public static Friendships getInstance() {
        if (instance == null)
            instance = new Friendships();

        return instance;
    }

    @Override
    public void print() {
        System.out.println("~Prietenii~");
        System.out.println("1. Afisare");
        System.out.println("2. Adaugare");
        System.out.println("3. Stergere");
        System.out.println("4. Numar de comunitati");
        System.out.println("5. Lungimea celei mai mari comunitati");
        System.out.println("6. Iesire");
    }

    @Override
    public void handle(String input) {
        switch (input) {
            case "1":
                ShowFriendships.getInstance().show();
                break;
            case "2":
                AddFriendship.getInstance().show();
                break;
            case "3":
                DeleteFriendship.getInstance().show();
                break;
            case "4":
                ShowNumberOfCommunities.getInstance().show();
                break;
            case "5":
                ShowLargestCommunity.getInstance().show();
                break;
            case "6":
                exitRequested = true;
                break;
            default:
                System.out.println("Optiune invalida");
        }
    }
}

class ShowFriendships extends AbstractUi {
    private static ShowFriendships instance;

    protected ShowFriendships() {}

    public static ShowFriendships getInstance() {
        if(instance == null)
            instance = new ShowFriendships();

        return instance;
    }

    @Override
    public void show() {
        print();
    }

    @Override
    public void print() {
        FriendshipsService.getInstance().getFriendships().forEach(System.out::println);
    }

    @Override
    public void handle(String input) { }
}

class AddFriendship extends AbstractUi {
    private static AddFriendship instance;
    private int step;
    private final FriendshipDTO friendshipDTO;

    protected AddFriendship() {
        step = 0;
        friendshipDTO = new FriendshipDTO();
    }

    public static AddFriendship getInstance() {
        if(instance == null)
            instance = new AddFriendship();

        return instance;
    }

    @Override
    public void show() {
        step = 0;
        super.show();
    }

    @Override
    public void print() {
        switch (step) {
            case 0:
                System.out.print("Id utilizator 1 = ");
                break;
            case 1:
                System.out.print("Id utilizator 2 = ");
                break;
            default:
                break;
        }
    }

    @Override
    public void handle(String input) {
        switch (step) {
            case 0:
                try {
                    friendshipDTO.user1 = Long.parseLong(input);
                }
                catch (NumberFormatException e) {
                    friendshipDTO.user1 = -1L;
                }

                step++;
                break;
            case 1:
                try {
                    friendshipDTO.user2 = Long.parseLong(input);
                }
                catch (NumberFormatException e) {
                    friendshipDTO.user2 = -1L;
                }

                step++;
                break;
        }

        if(step == 2) {
            try {
                FriendshipsService.getInstance().add(friendshipDTO);
            } catch (FriendshipException | UserException friendshipException) {
                System.out.println("Ati introdus date invalide");
            }
            exitRequested = true;
        }
    }
}

class ShowNumberOfCommunities extends AbstractUi {
    private static ShowNumberOfCommunities instance;

    protected ShowNumberOfCommunities() {}

    public static ShowNumberOfCommunities getInstance() {
        if(instance == null)
            instance = new ShowNumberOfCommunities();

        return instance;
    }

    @Override
    public void show() {
        print();
    }

    @Override
    public void print() {
        System.out.println("Numarul de comunitati: " + FriendshipsService.getInstance().getNumberOfCommunities());
    }

    @Override
    public void handle(String input) { }
}

class ShowLargestCommunity extends AbstractUi {
    private static ShowLargestCommunity instance;

    protected ShowLargestCommunity() {}

    public static ShowLargestCommunity getInstance() {
        if(instance == null)
            instance = new ShowLargestCommunity();

        return instance;
    }

    @Override
    public void show() {
        print();
    }

    @Override
    public void print() {
        System.out.println("Lungimea celei mai mari comunitati: " + FriendshipsService.getInstance().getMostSociableCommunity());
    }

    @Override
    public void handle(String input) { }
}

class DeleteFriendship extends AbstractUi {
    private static DeleteFriendship instance;

    protected DeleteFriendship() {}

    public static DeleteFriendship getInstance() {
        if(instance == null)
            instance = new DeleteFriendship();

        return instance;
    }

    @Override
    public void print() {
        System.out.println("Introduceti id-ul prieteniei: ");
    }

    @Override
    public void handle(String input) {
        try {
            long id = Long.parseLong(input);

            System.out.println(FriendshipsService.getInstance().removeById(id));
        } catch (NumberFormatException nfe) {
            System.out.println("Optiune invalida");
        } catch (FriendshipException re) {
            System.out.println("Prietenie negasita");
        }
        exitRequested = true;
    }
}

