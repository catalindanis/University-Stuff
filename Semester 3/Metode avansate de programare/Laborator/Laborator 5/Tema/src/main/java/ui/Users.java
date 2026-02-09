package ui;

import exceptions.UserException;
import models.DuckDTO;
import models.DuckType;
import models.PersonDTO;
import services.UsersService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Users extends AbstractUi {
    private static Users instance;

    protected Users() {}

    public static Users getInstance() {
        if (instance == null)
            instance = new Users();

        return instance;
    }

    @Override
    public void print() {
        System.out.println("~Useri~");
        System.out.println("1. Afisare");
        System.out.println("2. Adaugare");
        System.out.println("3. Stergere");
        System.out.println("4. Iesire");
    }

    @Override
    public void handle(String input) {
        switch (input) {
            case "1":
                ShowUsers.getInstance().show();
                break;
            case "2":
                AddUser.getInstance().show();
                break;
            case "3":
                DeleteUser.getInstance().show();
                break;
            case "4":
                exitRequested = true;
                break;
            default:
                System.out.println("Optiune invalida");
        }
    }
}

class ShowUsers extends AbstractUi {
    private static ShowUsers instance;

    protected ShowUsers() {}

    public static ShowUsers getInstance() {
        if(instance == null)
            instance = new ShowUsers();

        return instance;
    }

    @Override
    public void show() {
        print();
    }

    @Override
    public void print() {
        UsersService.getInstance().getUsers().forEach(System.out::println);
    }

    @Override
    public void handle(String input) { }
}

class AddUser extends AbstractUi {
    private static AddUser instance;
    private int userType;
    private int step;
    private final PersonDTO personDTO;
    private final DuckDTO duckDTO;
    private boolean skipInput;

    private AddUser() {
        step = 0;
        userType = 0;
        skipInput = false;

        personDTO = new PersonDTO();
        duckDTO = new DuckDTO();
    }

    public static AddUser getInstance() {
        if(instance == null)
            instance = new AddUser();

        return instance;
    }

    @Override
    public void show() {
        exitRequested = false;
        step = 0;
        skipInput = false;
        while(!exitRequested) {
            print();
            if(skipInput)
                handle("");
            else handle(input());
        }
    }

    @Override
    public void print() {
        switch (step) {
            case 0:
                System.out.println("Tip:");
                System.out.println("1. Persoana");
                System.out.println("2. Rata");
                break;
            case 1:
                System.out.print("Username = ");
                break;
            case 2:
                System.out.print("Email = ");
                break;
            case 3:
                System.out.print("Parola = ");
                break;
            case 4:
                if(userType == 1)
                    System.out.print("Prenume = ");
                else {
                    System.out.println("Tip rata:");
                    int optionNumber = 0;
                    for(var duckType : DuckType.values())
                        System.out.println(++optionNumber + ". " + duckType);
                }
                break;
            case 5:
                if(userType == 1)
                    System.out.print("Nume = ");
                else System.out.print("Viteza = ");
                break;
            case 6:
                if(userType == 1)
                    System.out.print("Zi de nastere (YYYY-MM-DD) = ");
                else System.out.print("Rezistenta = ");
                break;
            case 7:
                if(userType == 1)
                    System.out.print("Ocupatie = ");
                else skipInput = true;
                break;
            case 8:
                if(userType == 1)
                    System.out.print("Nivel de empatie = ");
                else skipInput = true;
                break;
        }
    }

    @Override
    public void handle(String input) {
        switch (step) {
            case 0:
                if(!input.equals("1") && !input.equals("2"))
                    break;

                step++;
                userType = Integer.parseInt(input);
                break;
            case 1:
                if(userType == 1)
                    personDTO.username = input;
                else
                    duckDTO.username = input;
                step++;
                break;
            case 2:
                if(userType == 1)
                    personDTO.email = input;
                else
                    duckDTO.email = input;
                step++;
                break;
            case 3:
                if(userType == 1)
                    personDTO.password = input;
                else
                    duckDTO.password = input;
                step++;
                break;
            case 4:
                if(userType == 1)
                    personDTO.firstName = input;
                else {
                    if(!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                        duckDTO.type = null;
                        break;
                    }

                    switch (input) {
                        case "1":
                            duckDTO.type = DuckType.FLYING;
                            break;
                        case "2":
                            duckDTO.type = DuckType.SWIMMING;
                            break;
                        case "3":
                            duckDTO.type = DuckType.FLYING_AND_SWIMMING;
                            break;
                    }
                }

                step++;
                break;
            case 5:
                if(userType == 1)
                    personDTO.lastName = input;
                else
                    try{
                        duckDTO.speed = Double.parseDouble(input);
                    } catch (NumberFormatException nfe) {
                        duckDTO.speed = 0;
                    }
                step++;
                break;
            case 6:
                if(userType == 1)
                    try {
                        personDTO.dateOfBirth = LocalDate.parse(input);
                    }
                    catch(DateTimeParseException dtpe){
                        personDTO.dateOfBirth = null;
                    }
                else
                    try{
                        duckDTO.resistance = Double.parseDouble(input);
                    } catch (NumberFormatException nfe) {
                        duckDTO.resistance = 0;
                    }
                step++;
                break;
            case 7:
                if(userType == 1)
                    personDTO.occupation = input;
                step++;
                break;
            case 8:
                if(userType == 1)
                    try {
                        personDTO.empathyLevel = Integer.parseInt(input);
                    }
                    catch (NumberFormatException nfe) {
                        personDTO.empathyLevel = -1;
                    }

                step++;
                skipInput = true;
                break;
        }

        if(step == 9) {
            if(userType == 1) {
                try {
                    UsersService.getInstance().add(personDTO);
                } catch (UserException ue) {
                    System.out.println("Ati introdus date invalide");
                }
            }
            else {
                try {
                    UsersService.getInstance().add(duckDTO);
                } catch (UserException ue) {
                    System.out.println("Ati introdus date invalide");
                }
            }
            exitRequested = true;
        }
    }
}

class DeleteUser extends AbstractUi {
    private static DeleteUser instance;

    protected DeleteUser() {}

    public static DeleteUser getInstance() {
        if(instance == null)
            instance = new DeleteUser();

        return instance;
    }

    @Override
    public void print() {
        System.out.println("Introduceti id-ul userului: ");
    }

    @Override
    public void handle(String input) {
        try {
            long id = Long.parseLong(input);

            System.out.println(UsersService.getInstance().removeById(id));
        } catch (NumberFormatException nfe) {
            System.out.println("Optiune invalida");
        } catch (UserException re) {
            System.out.println("Utilizator negasit");
        }
        exitRequested = true;
    }
}