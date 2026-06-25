package services;

import dtos.GameResponse;
import dtos.NewGameRequest;
import dtos.NewMoveRequest;
import dtos.RoundResponse;
import entities.Configuration;
import entities.Game;
import entities.Move;
import entities.Player;
import lombok.RequiredArgsConstructor;
import mappers.ConfigurationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import repositories.AbstractJPARepository;
import repositories.ConfigurationsRepository;
import repositories.PlayersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class PlayersService implements Service {
    private final Logger logger = LogManager.getLogger();

    private final AbstractJPARepository<Long, Player> usersRepository;
    private final AbstractJPARepository<Long, Game> gamesRepository;
    private final AbstractJPARepository<Long, Move> movesRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConfigurationsService configurationsService;
    private final ConfigurationMapper configurationMapper;
    private final ConfigurationsRepository configurationsRepository;
    private final PlayersRepository playersRepository;

    @Value("${application.numberOfPlayers}")
    private Integer numberOfPlayers;

    public List<GameResponse> getGamesStatsForUser(Long userId) {
        logger.traceEntry();

        Player player = usersRepository.findOne(userId);
        if(player == null) {
            logger.error("Player with id {} not found", userId);
            throw new RuntimeException("Player with id " + userId + " not found");
        }
        List<GameResponse> gameStats = new ArrayList<>();

        for(Game game : player.getGames()) {
            List<Move> moves = game.getMoves().stream().filter(move -> move.getPlayer().getId().equals(userId)).toList();
            int numberOfPointsFinal = moves.stream().mapToInt(Move::getPoints).sum();

            if(numberOfPointsFinal >= 5) {
                int numberOfRounds = game.getConfiguration().getNumberOfPlayers();

                List<RoundResponse> roundsResponse = new ArrayList<>();
                for(Move move : moves) {
                    roundsResponse.add(new RoundResponse(move.getPosition(), move.getPoints()));
                }

                gameStats.add(new GameResponse(numberOfRounds, roundsResponse));
            }
        }

        logger.traceExit(gameStats);
        return gameStats;
    }

    public void joinGame() {
        List<SimpUser> users = simpUserRegistry.getUsers().stream().toList();
        logger.trace("User count: {} / {}", users.size(), numberOfPlayers);

        if(users.size() == numberOfPlayers) {
            List<Configuration> configurations = configurationsService.findAllEntities();
            List<Configuration> chosenConfigurations = configurations.stream()
                    .filter(configuration -> configuration.getNumberOfPlayers() == numberOfPlayers)
                    .limit(3)
                    .toList();

            messagingTemplate.convertAndSend(
                    "/topic/updates",
                    Map.of(
                            "status", "starting",
                            "configurations", configurationMapper.toDTO(chosenConfigurations)
                    )
            );
            messagingTemplate.convertAndSendToUser(
                    users.get(users.size() / 2).getName(),
                    "/queue/private",
                    Map.of(
                            "turn", "true"
                    )
            );
        }
    }

    public void createNewGame(NewGameRequest newGameRequest) {
        Game game = new Game();
        Configuration configuration = configurationsRepository.findOne(newGameRequest.configurationId());
        game.setConfiguration(configuration);

        List<SimpUser> simpUsers = simpUserRegistry.getUsers().stream().toList();
        List<Player> players = new ArrayList<>();
        for(SimpUser simpUser : simpUsers) {
            playersRepository.findByNickname(simpUser.getName()).ifPresent(players::add);
        }

        game.setPlayers(players);

        Long gameId = gamesRepository.save(game);
        messagingTemplate.convertAndSend(
                "/topic/updates",
                Map.of(
                        "status", "started",
                        "configuration", configuration.getPoints(),
                        "round", 1,
                        "gameId", gameId
                )
        );
        messagingTemplate.convertAndSendToUser(
                simpUsers.getFirst().getName(),
                "/queue/private",
                Map.of(
                        "turn", "true"
                )
        );
    }

    public void createNewMove(NewMoveRequest newMoveRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player user = playersRepository.findByNickname(authentication.getName()).orElse(null);

        if(user != null) {
            Game game = gamesRepository.findOne(newMoveRequest.gameId());

            List<Move> moves = game.getMoves();
            List<Move> userMoves = moves.stream()
                    .filter(move -> move.getPlayer().getId() == user.getId())
                    .sorted(Comparator.comparingInt(Move::getRound))
                    .toList();

            int previousPosition = 0;
            if(!userMoves.isEmpty())
              previousPosition = userMoves.getLast().getPosition();

            int newPosition = (previousPosition + newMoveRequest.generatedNumber()) % (2 * numberOfPlayers);

            Move move = new Move();

            move.setPlayer(user);
            move.setGame(game);
            move.setRound(newMoveRequest.round());
            move.setPoints(game.getConfiguration().getPoints().get(newPosition));
            move.setPosition(newPosition);
            move.setGeneratedNumber(newMoveRequest.generatedNumber());

            moves.add(move);
            game.setMoves(moves);

            movesRepository.save(move);
            gamesRepository.update(game);

            messagingTemplate.convertAndSend(
                    "/topic/updates",
                    Map.of(
                            "status", "ongoing",
                            "round", newMoveRequest.round() + 1,
                            "lastGeneratedNumber", newMoveRequest.generatedNumber(),
                            "lastPlayer", user.getNickname(),
                            "lastPlayerPosition", newPosition,
                            "lastMovePoints", move.getPoints()
                    )
            );

            List<SimpUser> simpUsers = simpUserRegistry.getUsers().stream().toList();
            int nextUserPosition = 0;
            for(int i = 0; i < simpUsers.size() - 1; i++) {
                if(simpUsers.get(i).getName().equals(user.getNickname())) {
                    nextUserPosition = i + 1;
                    break;
                }
            }
            messagingTemplate.convertAndSendToUser(
                    simpUsers.get(nextUserPosition).getName(),
                    "/queue/private",
                    Map.of(
                            "turn", "true"
                    )
            );
        }
    }
}
