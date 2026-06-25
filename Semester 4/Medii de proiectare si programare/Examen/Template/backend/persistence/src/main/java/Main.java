import entities.*;
import repositories.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PlayersRepository playersRepository = new PlayersRepository();

        Player player = new Player();
        player.setNickname("player1");
        player.setAge(18);

        Player player2 = new Player();
        player2.setNickname("player2");
        player2.setAge(22);

        Player player3 = new Player();
        player3.setNickname("player3");
        player3.setAge(24);

        playersRepository.save(player);
        playersRepository.save(player2);
        playersRepository.save(player3);

        CategoriesRepository categoriesRepository = new CategoriesRepository();

        Category category = new Category();
        category.setName("category1");

        Answer answer = new Answer();
        answer.setCategory(category);
        answer.setText("answer1");
        category.setAnswers(List.of(answer));

        Category category2 = new Category();
        category2.setName("category2");

        Answer answer2 = new Answer();
        answer2.setCategory(category2);
        answer2.setText("answer2");
        category2.setAnswers(List.of(answer2));

        Category category3 = new Category();
        category3.setName("category3");

        Answer answer3 = new Answer();
        answer3.setCategory(category3);
        answer3.setText("answer3");
        category3.setAnswers(List.of(answer3));

        categoriesRepository.save(category);
        categoriesRepository.save(category2);
        categoriesRepository.save(category3);

        GamesRepository gamesRepository = new GamesRepository();

        Game game = new Game();
        game.setCategories(List.of(category, category2, category3));
        game.setPlayers(List.of(player, player2, player3));

        gamesRepository.save(game);

        Game game2 = new Game();
        game2.setCategories(List.of(category, category2, category3));
        game2.setPlayers(List.of(player, player2, player3));

        gamesRepository.save(game2);

        Move move = new Move();
        move.setPlayer(player);
        move.setRound(1);
        move.setPoints(10);
        move.setGame(game);
        move.setAnswer("answer1");

        Move move2 = new Move();
        move2.setPlayer(player2);
        move2.setRound(1);
        move2.setPoints(5);
        move2.setGame(game);
        move2.setAnswer("answer2");

        Move move3 = new Move();
        move3.setPlayer(player3);
        move3.setRound(1);
        move3.setPoints(0);
        move3.setGame(game);
        move3.setAnswer("answer3");

        Move move4 = new Move();
        move4.setPlayer(player);
        move4.setRound(2);
        move4.setPoints(5);
        move4.setGame(game);
        move4.setAnswer("answer2");

        Move move5 = new Move();
        move5.setPlayer(player2);
        move5.setRound(2);
        move5.setPoints(10);
        move5.setGame(game);
        move5.setAnswer("answer3");

        Move move6 = new Move();
        move6.setPlayer(player3);
        move6.setRound(2);
        move6.setPoints(5);
        move6.setGame(game);
        move6.setAnswer("answer1");


        MovesRepository movesRepository = new MovesRepository();
        movesRepository.save(move);
        movesRepository.save(move2);
        movesRepository.save(move3);
        movesRepository.save(move4);
        movesRepository.save(move5);
        movesRepository.save(move6);

        AnswersRepositories answersRepositories = new AnswersRepositories();
        answersRepositories.save(answer);
        answersRepositories.save(answer2);
        answersRepositories.save(answer3);
    }
}
