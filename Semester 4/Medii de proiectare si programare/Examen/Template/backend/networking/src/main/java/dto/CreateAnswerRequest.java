package dto;

public record CreateAnswerRequest(
        String category,
        String answer,
        int numberOfPoints
) {
}
