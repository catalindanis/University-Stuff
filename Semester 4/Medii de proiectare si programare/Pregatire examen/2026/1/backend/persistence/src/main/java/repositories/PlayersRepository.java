package repositories;

import entities.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import utils.JpaUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class PlayersRepository extends AbstractJPARepository<Long, Player> {
    private final Logger logger = LogManager.getLogger();

    public PlayersRepository() {
        super(Player.class);
    }

    public Optional<Player> findByNickname(String nickname) {
        logger.traceEntry();
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Player> query = entityManager.createQuery(
                    "SELECT p FROM Player p WHERE p.nickname = :nickname", Player.class);
            query.setParameter("nickname", nickname);

            List<Player> resultList = query.getResultList();
            if (resultList.isEmpty()) {
                logger.traceExit("No player found with nickname: {}", nickname);
                return Optional.empty();
            } else {
                Player player = resultList.getFirst();
                logger.traceExit(player);
                return Optional.of(player);
            }
        }
    }
}
