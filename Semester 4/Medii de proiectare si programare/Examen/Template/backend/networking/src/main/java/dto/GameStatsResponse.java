package dto;

import java.util.List;

public record GameStatsResponse(
        Long playerId,
        List<PlayerMoveResponse> rounds
) {
}
