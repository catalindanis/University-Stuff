package repositories;

import entities.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigurationsRepository extends AbstractJPARepository<Long, Configuration> {
    private final Logger logger = LogManager.getLogger();

    public ConfigurationsRepository() {
        super(Configuration.class);
    }
}
