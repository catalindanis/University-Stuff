package dtos;

public record NewMoveRequest(
        Long gameId,
        Integer generatedNumber,
        Integer round
) {
}
