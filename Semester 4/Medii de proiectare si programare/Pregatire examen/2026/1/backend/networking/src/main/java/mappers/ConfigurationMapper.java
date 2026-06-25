package mappers;

import dtos.ConfigurationResponse;
import entities.Configuration;

public class ConfigurationMapper implements Mapper<Configuration, ConfigurationResponse> {
    @Override
    public ConfigurationResponse toDTO(Configuration configuration) {
        return new ConfigurationResponse(
                configuration.getId(),
                configuration.getPoints(),
                configuration.getNumberOfPlayers()
        );
    }
}
