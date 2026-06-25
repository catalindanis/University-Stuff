package controllers;

import dto.CreateAnswerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.AnswersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
public class AnswersController {

    @Autowired
    private final AnswersService answersService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity save(@RequestBody CreateAnswerRequest request) {
        try {
            var result = answersService.save(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid category");
        }
    }
}
