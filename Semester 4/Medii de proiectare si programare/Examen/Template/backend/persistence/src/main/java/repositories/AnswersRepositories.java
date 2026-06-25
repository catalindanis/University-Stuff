package repositories;

import entities.Answer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class AnswersRepositories extends AbstractJPARepository<Long, Answer> {
    private final Logger logger = LogManager.getLogger();

    public AnswersRepositories() {
        super(Answer.class);
    }
}
