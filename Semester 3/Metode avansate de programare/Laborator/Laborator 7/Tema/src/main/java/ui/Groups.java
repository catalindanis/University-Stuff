package ui;

import exceptions.GroupException;
import models.DuckType;
import dto.GroupDTO;
import services.GroupsService;

import java.util.Arrays;

public class Groups extends AbstractUi {
    private static final Groups instance = new Groups();

    private Groups() {}

    public static Groups getInstance() { return instance; }

    @Override
    public void print() {
        System.out.println("~Carduri~");
        System.out.println("1. Afisare");
        System.out.println("2. Adaugare");
        System.out.println("3. Stergere");
        System.out.println("4. Iesire");
    }

    @Override
    public void handle(String input) {
        switch(input) {
            case "1":
                ShowGroups.getInstance().show();
                break;
            case "2":
                AddGroup.getInstance().show();
                break;
            case "3":
                DeleteGroup.getInstance().show();
                break;
            case "4":
                exitRequested = true;
                break;
            default:
                System.out.println("Optiune invalida");
        }
    }
}

class ShowGroups extends AbstractUi {
    private static final ShowGroups instance = new ShowGroups();

    private ShowGroups() {}

    public static ShowGroups getInstance() { return instance; }

    @Override
    public void show() { print(); }

    @Override
    public void print() { GroupsService.getInstance().getGroups().forEach(System.out::println); }

    @Override
    public void handle(String input) {}
}

class AddGroup extends AbstractUi {
    private static final AddGroup instance = new AddGroup();
    private int step;
    private final GroupDTO groupDTO;

    private AddGroup() {
        step = 0;

        groupDTO = new GroupDTO();
    }

    public static AddGroup getInstance() { return instance; }

    @Override
    public void show() {
        exitRequested = false;
        step = 0;
        super.show();
    }

    @Override
    public void print() {
        switch(step) {
            case 0:
                System.out.print("Nume = ");
                break;
            case 1:
                System.out.println("Tip card:");
                int optionNumber = 0;
                for(var duckType : DuckType.values())
                    System.out.println(++optionNumber + ". " + duckType);
                break;
            case 2:
                System.out.print("Id-uri rate (separate prin virgula) = ");
                break;
        }
    }

    @Override
    public void handle(String input) {
        switch(step) {
            case 0:
                groupDTO.name = input;
                step++;
                break;
            case 1:
                if(!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                    groupDTO.ducksType = null;
                    break;
                }

                switch (input) {
                    case "1":
                        groupDTO.ducksType = DuckType.FLYING;
                        break;
                    case "2":
                        groupDTO.ducksType = DuckType.SWIMMING;
                        break;
                    case "3":
                        groupDTO.ducksType = DuckType.FLYING_AND_SWIMMING;
                        break;
                }
                step++;
                break;
            case 2:
                try {
                    groupDTO.ducksIds = Arrays.stream(input.split(",")).map(Long::parseLong).toList();
                }
                catch (NumberFormatException e) {
                    groupDTO.ducksIds = null;
                }
                step++;
                break;
        }

        if(step == 3) {
            try {
                GroupsService.getInstance().add(groupDTO);
            }
            catch (GroupException e) {
                System.out.println("Ati introdus date invalide");
            }
            exitRequested = true;
        }
    }
}

class DeleteGroup extends AbstractUi {
    private static final DeleteGroup instance = new DeleteGroup();

    private DeleteGroup() {}

    public static DeleteGroup getInstance() { return instance; }

    @Override
    public void print() {
        System.out.print("Introduceti id-ul cardului: ");
    }

    @Override
    public void handle(String input) {
        try {
            long id = Long.parseLong(input);

            System.out.println(GroupsService.getInstance().removeById(id));
        } catch (NumberFormatException nfe) {
            System.out.println("Optiune invalida");
        } catch (GroupException re) {
            System.out.println("Card negasit");
        }
        exitRequested = true;
    }
}