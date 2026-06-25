package repositories;

import entities.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriesRepository extends AbstractJPARepository<Long, Category> {
    private final Logger logger = LogManager.getLogger();

    public CategoriesRepository() {
        super(Category.class);
    }
}