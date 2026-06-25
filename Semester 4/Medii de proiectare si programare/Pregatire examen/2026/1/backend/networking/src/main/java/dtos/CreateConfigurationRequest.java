package dtos;

import java.util.List;

public record CreateConfigurationRequest(
        List<Integer> points,
        int numberOfPlayers
) {
}
