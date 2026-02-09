import domain.*;
import repository.*;
import service.SocialNetworkService;
import ui.Console;
import validation.*;

public class Main {

    public static void main(String[] args) {

        Repository<Long, Persoana> persoanaRepository = new PersoanaDBRepository("jdbc:postgresql://localhost:5432/duck_social_network", "postgres", "calin2005");
        Repository<Long, Duck> duckRepository = new DuckDBRepository("jdbc:postgresql://localhost:5432/duck_social_network", "postgres", "calin2005");
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/duck_social_network", "postgres", "calin2005");
        Repository<Long, Card> cardRepository = new CardDBRepository("jdbc:postgresql://localhost:5432/duck_social_network", "postgres", "calin2005");
        EventDBRepository eventRepository = new EventDBRepository("jdbc:postgresql://localhost:5432/duck_social_network", "postgres", "calin2005");

        Validator<User> userValidator = new UserValidator();
        Validator<Persoana> persoanaValidator = new PersoanaValidator();
        Validator<Duck> duckValidator = new DuckValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Validator<Card> cardValidator = new CardValidator();
        Validator<Event> eventValidator = new EventValidator();

        SocialNetworkService service = new SocialNetworkService(
                persoanaRepository,
                duckRepository,
                friendshipRepository,
                cardRepository,
                eventRepository,
                userValidator,
                persoanaValidator,
                duckValidator,
                friendshipValidator,
                cardValidator,
                eventValidator
        );

        Console console = new Console(service);

        console.run();
    }
}