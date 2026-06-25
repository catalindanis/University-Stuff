package repositories;

import entities.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class MovesRepository extends AbstractJPARepository<Long, Move> {
    private final Logger logger = LogManager.getLogger();

    public MovesRepository() {
        super(Move.class);
    }
}
