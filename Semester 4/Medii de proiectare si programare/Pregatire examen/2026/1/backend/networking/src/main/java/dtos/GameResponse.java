package dtos;

import java.util.List;

public record GameResponse(
        int numberOfRounds,
        List<RoundResponse> rounds
) {
}
