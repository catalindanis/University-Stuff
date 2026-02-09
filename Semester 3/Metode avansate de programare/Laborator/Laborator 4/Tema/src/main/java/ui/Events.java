package ui;

import exceptions.EventException;
import exceptions.UserException;
import models.Lane;
import models.RaceEventDTO;
import services.EventsService;

import java.util.Arrays;

public class Events extends AbstractUi {
    public static final Events instance = new Events();

    private Events() {}

    public static Events getInstance() { return instance; }

    @Override
    public void print() {
        System.out.println("~Evenimente~");
        System.out.println("1. Afisare");
        System.out.println("2. Adaugare");
        System.out.println("3. Executare");
        System.out.println("4. Abonare");
        System.out.println("5. Iesire");
    }

    @Override
    public void handle(String input) {
        switch (input) {
            case "1":
                ShowEvents.getInstance().show();
                break;
            case "2":
                AddEvent.getInstance().show();
                break;
            case "3":
                ExecuteEvent.getInstance().show();
                break;
            case "4":
                SubscribeEvent.getInstance().show();
                break;
            case "5":
                exitRequested = true;
                break;
            default:
                System.out.println("Optiune invalida");
        }
    }
}

class ShowEvents extends AbstractUi {
    private static final ShowEvents instance = new ShowEvents();

    private ShowEvents() {}

    public static ShowEvents getInstance() { return instance; }

    @Override
    public void show() { print(); }

    @Override
    public void print() { EventsService.getInstance().getEvents().forEach(System.out::println); }

    @Override
    public void handle(String input) {}
}

class AddEvent extends AbstractUi {
    private static final AddEvent instance = new AddEvent();
    private int step;
    private final RaceEventDTO raceEventDTO;

    private AddEvent() {
        step = 0;

        raceEventDTO = new RaceEventDTO();
    }

    public static AddEvent getInstance() { return instance; }

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
                System.out.print("Id card = ");
                break;
            case 1:
                System.out.print("Numar culoare = ");
                break;
            case 2:
                System.out.print("Lungimi culoare (separate prin virgula) = ");
                break;
        }
    }

    @Override
    public void handle(String input) {
        switch(step) {
            case 0:
                try {
                    raceEventDTO.swimmingGroupId = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    raceEventDTO.swimmingGroupId = -1;
                }
                step++;
                break;
            case 1:
                try {
                    raceEventDTO.noLanes = Integer.parseInt(input);
                }
                catch (NumberFormatException e) {
                    raceEventDTO.noLanes = -1;
                }
                step++;
                break;
            case 2:
                try {
                    raceEventDTO.lanes = Arrays.stream(input.split(",")).map(Integer::parseInt).map(Lane::new).toList();
                    if(raceEventDTO.lanes.size() != raceEventDTO.noLanes)
                        throw new NumberFormatException();
                }
                catch (NumberFormatException e) {
                    raceEventDTO.lanes = null;
                }
                step++;
                break;
        }

        if(step == 3) {
            try {
                EventsService.getInstance().add(raceEventDTO);
            }
            catch (EventException e) {
                System.out.println("Ati introdus date invalide");
            }
            exitRequested = true;
        }
    }
}

class ExecuteEvent extends AbstractUi {
    private static final ExecuteEvent instance = new ExecuteEvent();

    private ExecuteEvent() {}

    public static ExecuteEvent getInstance() { return instance; }

    @Override
    public void print() { System.out.print("Introduceti id-ul evenimentului: "); }

    @Override
    public void handle(String input) {
        try {
            long id = Long.parseLong(input);
            System.out.println(EventsService.getInstance().start(id).toString());
        } catch (NumberFormatException e) {
            System.out.println("Ati introdus date invalide");
        } catch (NullPointerException e) {
            System.out.println("Eveniment negasit");
        }
        exitRequested = true;
    }
}

class SubscribeEvent extends AbstractUi {
    private static final SubscribeEvent instance = new SubscribeEvent();
    private int step;
    private long userId;

    private SubscribeEvent() { step = 0; }

    public static SubscribeEvent getInstance() { return instance; }

    @Override
    public void show() {
        step = 0;
        exitRequested = false;
        super.show();
    }

    @Override
    public void print() {
        switch (step) {
            case 0:
                System.out.print("Introduceti id-ul user-ului: ");
                break;
            case 1:
                System.out.print("Introduceti id-ul event-ului: ");
                break;
        }
    }

    @Override
    public void handle(String input) {
        try {
            if(step == 0) {
                userId = Long.parseLong(input);
                step++;
            }
            else {
                long eventId = Long.parseLong(input);
                exitRequested = true;
                EventsService.getInstance().subscribe(userId, eventId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ati introdus date invalide");
        }
        catch (UserException | EventException e) {
            System.out.println(e.getMessage());
        }
    }
}

