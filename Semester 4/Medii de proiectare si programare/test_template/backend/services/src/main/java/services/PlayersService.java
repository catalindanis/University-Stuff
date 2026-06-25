//package services;
//
//import dtos.GameResponse;
//import dtos.RoundResponse;
//import entities.Game;
//import entities.Move;
//import entities.Player;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import repositories.AbstractJPARepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@org.springframework.stereotype.Service
//public class PlayersService implements Service {
//    private final Logger logger = LogManager.getLogger();
//
//    private final AbstractJPARepository<Long, Player> usersRepository;
//    private final AbstractJPARepository<Long, Game> gamesRepository;
//    private final AbstractJPARepository<Long, Move> movesRepository;
//
//    public PlayersService(AbstractJPARepository<Long, Player> usersRepository, AbstractJPARepository<Long, Game> gamesRepository, AbstractJPARepository<Long, Move> movesRepository) {
//        this.usersRepository = usersRepository;
//        this.gamesRepository = gamesRepository;
//        this.movesRepository = movesRepository;
//    }
//
//    public List<GameResponse> getGamesStatsForUser(Long userId) {
//        logger.traceEntry();
//
//        Player player = usersRepository.findOne(userId);
//        if(player == null) {
//            logger.error("Player with id {} not found", userId);
//            throw new RuntimeException("Player with id " + userId + " not found");
//        }
//        List<GameResponse> gameStats = new ArrayList<>();
//
//        for(Game game : player.getGames()) {
//            List<Move> moves = game.getMoves().stream().filter(move -> move.getPlayer().getId().equals(userId)).toList();
//            int numberOfPointsFinal = moves.stream().mapToInt(Move::getPoints).sum();
//
//            if(numberOfPointsFinal >= 5) {
//                int numberOfRounds = game.getConfiguration().getNumberOfPlayers();
//
//                List<RoundResponse> roundsResponse = new ArrayList<>();
//                for(Move move : moves) {
//                    roundsResponse.add(new RoundResponse(move.getPosition(), move.getPoints()));
//                }
//
//                gameStats.add(new GameResponse(numberOfRounds, roundsResponse));
//            }
//        }
//
//        logger.traceExit(gameStats);
//        return gameStats;
//    }
//}
