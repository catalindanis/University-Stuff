package repositories;

import entities.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class GamesRepository extends AbstractJPARepository<Long, Game> {
    private final Logger logger = LogManager.getLogger();

    public GamesRepository() {
        super(Game.class);
    }
}