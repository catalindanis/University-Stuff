//package services;
//
//import dtos.ConfigurationResponse;
//import dtos.CreateConfigurationRequest;
//import entities.Configuration;
//import mappers.ConfigurationMapper;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import repositories.AbstractJPARepository;
//
//import java.util.List;
//import java.util.Map;
//
//@org.springframework.stereotype.Service
//public class ConfigurationsService implements Service {
//    private final Logger logger = LogManager.getLogger();
//
//    private final AbstractJPARepository<Long, Configuration> repository;
//    private final ConfigurationMapper configurationMapper;
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @Value("${application.numberOfPlayers}")
//    private Integer numberOfPlayers;
//
//    @Autowired
//    public ConfigurationsService(AbstractJPARepository<Long, Configuration> repository, ConfigurationMapper configurationMapper, SimpMessagingTemplate messagingTemplate) {
//        this.repository = repository;
//        this.configurationMapper = configurationMapper;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public Long save(CreateConfigurationRequest request) {
//        logger.traceEntry();
//        Configuration configuration = new Configuration(
//                null,
//                request.numberOfPlayers(),
//                request.points()
//        );
//
//        validateRequest(request);
//
//        logger.traceExit();
//        Long save = repository.save(configuration);
//
//        messagingTemplate.convertAndSend("/topic/updates", Map.of(
//                "id", save
//        ));
//        messagingTemplate.convertAndSendToUser(
//                "player1",
//                "/queue/private",
//                Map.of(
//                        "type", "CONFIGURATION_CREATED",
//                        "id", save
//                )
//        );
//        return save;
//    }
//
//    private void validateRequest(CreateConfigurationRequest request) {
//        if(request.numberOfPlayers() <= 0 || request.points() == null || request.numberOfPlayers() * 2 != request.points().size())
//            throw new RuntimeException();
//    }
//
//    public List<ConfigurationResponse> findAll() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        logger.warn(authentication.getName() + " calls findAll() method");
//
//        return configurationMapper.toDTO(repository.findAll());
//    }
//}
