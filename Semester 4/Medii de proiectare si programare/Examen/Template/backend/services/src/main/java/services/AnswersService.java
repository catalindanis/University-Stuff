package services;

import dto.CreateAnswerRequest;
import entities.Answer;
import entities.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repositories.AbstractJPARepository;

import java.util.List;

@Service
public class AnswersService {
    private final Logger logger = LogManager.getLogger();

    private final AbstractJPARepository<Long, Answer> answersRepository;
    private final AbstractJPARepository<Long, Category> categoriesRepositories;

    @Autowired
    public AnswersService(AbstractJPARepository<Long, Answer> answersRepository, AbstractJPARepository<Long, Category> categoriesRepositories) {
        this.answersRepository = answersRepository;
        this.categoriesRepositories = categoriesRepositories;
    }

    public Long save(CreateAnswerRequest request) {
        logger.traceEntry();

        Category category = validateRequest(request);

        Answer answer = new Answer();
        answer.setText(request.answer());
        answer.setNumberOfPoints(request.numberOfPoints());
        answer.setCategory(category);

        Long save = answersRepository.save(answer);
        logger.traceExit(save);
        return save;
    }

    private Category validateRequest(CreateAnswerRequest request) {
        List<Category> categories = categoriesRepositories.findAll();

        for(Category category : categories) {
            if(category.getName().equals(request.category()))
                return category;
        }

        throw new RuntimeException("Category does not exist");
    }
}
