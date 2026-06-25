import entities.Configuration;
import entities.Game;
import entities.Move;
import entities.Player;
import repositories.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Repository<Long, Player> playersRepo = new PlayersRepository();
        Repository<Long, Game> gameRepository = new GamesRepository();
        Repository<Long, Configuration> configurationRepository = new ConfigurationsRepository();
        Repository<Long, Move> moveRepository = new MovesRepository();

        playersRepo.save(new Player(
                null,
                "player1"
        ));

        playersRepo.save(new Player(
                null,
                "player2"
        ));

        playersRepo.save(new Player(
                null,
                "player3"
        ));

        Configuration configuration = new Configuration();
        configuration.setNumberOfPlayers(3);
        configuration.setPoints(List.of(1, 2, 3, 4, 5, 6));
        configurationRepository.save(configuration);

        Player player1 = playersRepo.findOne(1L);
        Player player2 = playersRepo.findOne(2L);
        Player player3 = playersRepo.findOne(3L);

        Game game = new Game();
        game.setConfiguration(configuration);
        game.setPlayers(List.of(player1, player2, player3));
        gameRepository.save(game);

        Move move1 = new Move();
        move1.setPlayer(player1);
        move1.setPosition(1);
        move1.setPoints(1);
        move1.setRound(1);
        move1.setGame(game);

        Move move2 = new Move();
        move2.setPlayer(player2);
        move2.setPosition(3);
        move2.setPoints(3);
        move2.setRound(1);
        move2.setGame(game);

        Move move3 = new Move();
        move3.setPlayer(player3);
        move3.setPosition(6);
        move3.setPoints(6);
        move3.setRound(1);
        move3.setGame(game);

        moveRepository.save(move1);
        moveRepository.save(move2);
        moveRepository.save(move3);

        Move move4 = new Move();
        move4.setPlayer(player1);
        move4.setPosition(3);
        move4.setPoints(-1);
        move4.setRound(2);
        move4.setGame(game);

        Move move5 = new Move();
        move5.setPlayer(player2);
        move5.setPosition(5);
        move5.setPoints(5);
        move5.setRound(2);
        move5.setGame(game);

        Move move6 = new Move();
        move6.setPlayer(player3);
        move6.setPosition(1);
        move6.setPoints(1);
        move6.setRound(2);
        move6.setGame(game);

        moveRepository.save(move4);
        moveRepository.save(move5);
        moveRepository.save(move6);

        Move move7 = new Move();
        move7.setPlayer(player1);
        move7.setPosition(5);
        move7.setPoints(5);
        move7.setRound(3);
        move7.setGame(game);

        Move move8 = new Move();
        move8.setPlayer(player2);
        move8.setPosition(2);
        move8.setPoints(2);
        move8.setRound(3);
        move8.setGame(game);

        Move move9 = new Move();
        move9.setPlayer(player3);
        move9.setPosition(4);
        move9.setPoints(-3);
        move9.setRound(3);
        move9.setGame(game);

        moveRepository.save(move7);
        moveRepository.save(move8);
        moveRepository.save(move9);
    }
}
