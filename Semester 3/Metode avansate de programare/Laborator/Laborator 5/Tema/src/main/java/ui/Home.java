package ui;

public class Home extends AbstractUi {
    private static Home instance;

    protected Home() {}

    public static Home getInstance() {
        if (instance == null)
            instance = new Home();

        return instance;
    }

    @Override
    public void print() {
        System.out.println("~Meniu principal~");
        System.out.println("1. Useri");
        System.out.println("2. Prietenii");
        System.out.println("3. Carduri");
        System.out.println("4. Evenimente");
        System.out.println("5. Iesire");
    }

    @Override
    public void handle(String input) {
        switch (input) {
            case "1":
                Users.getInstance().show();
                break;
            case "2":
                Friendships.getInstance().show();
                break;
            case "3":
                Groups.getInstance().show();
                break;
            case "4":
                Events.getInstance().show();
                break;
            case "5":
                exitRequested = true;
                break;
            default:
                System.out.println("Optiune invalida");
        }
    }
}
