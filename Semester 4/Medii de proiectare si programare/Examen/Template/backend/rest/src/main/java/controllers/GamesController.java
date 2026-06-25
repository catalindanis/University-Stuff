package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.GamesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private final GamesService gamesService;

    @GetMapping(path = "/{id}/stats", produces = "application/json")
    public ResponseEntity save(@PathVariable("id") Long gameId) {
        try {
            var result = gamesService.getStatsForGame(gameId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid game");
        }
    }

    @PostMapping("/join-game")
    public void joinGame() {
        gamesService.joinGame();
    }
}
