package services;

import dto.GameStatsResponse;
import dto.PlayerMoveResponse;
import entities.Answer;
import entities.Game;
import entities.Move;
import entities.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import repositories.AbstractJPARepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GamesService {
    private final Logger logger = LogManager.getLogger();
    private final AbstractJPARepository<Long, Game> gamesRepository;
    private final AbstractJPARepository<Long, Answer> answersRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${application.numberOfPlayers}")
    private Integer numberOfPlayers;

    @Autowired
    public GamesService(AbstractJPARepository<Long, Game> gamesRepository, AbstractJPARepository<Long, Answer> answersRepository, SimpUserRegistry simpUserRegistry, SimpMessagingTemplate simpMessagingTemplate) {
        this.gamesRepository = gamesRepository;
        this.answersRepository = answersRepository;
        this.simpUserRegistry = simpUserRegistry;
        this.messagingTemplate = simpMessagingTemplate;
    }

    public List<GameStatsResponse> getStatsForGame(Long gameId) {
        Game game = gamesRepository.findOne(gameId);

        List<GameStatsResponse> result = new ArrayList<>();

        for(Player player : game.getPlayers()) {

            List<PlayerMoveResponse> playerMoves = new ArrayList<>();

            for(Move move : game.getMoves()) {
                if(move.getPlayer().getId().equals(player.getId())) {
                    if(move.getPoints() <= 0)
                        continue;

                    playerMoves.add(new PlayerMoveResponse(
                       move.getAnswer(),
                       move.getPoints()
                    ));
                }
            }

            if(playerMoves.size() >= numberOfPlayers / 2)
                result.add(
                        new GameStatsResponse(
                                player.getId(),
                                playerMoves
                        )
                );
        }

        return result;
    }

    public void joinGame() {
        List<SimpUser> users = simpUserRegistry.getUsers().stream().toList();
        logger.trace("User count: {} / {}", users.size(), numberOfPlayers);

        if(users.size() == numberOfPlayers) {
            messagingTemplate.convertAndSend(
                    "/topic/updates",
                    Map.of(
                            "status", "starting"
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
}
