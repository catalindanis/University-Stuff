package controllers;

import dtos.NewGameRequest;
import dtos.NewMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.PlayersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayersController {

    private final PlayersService playersService;

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getUserGamesStats(@PathVariable("id") Long userId) {
        try {
            return ResponseEntity
                        .ok(playersService.getGamesStatsForUser(userId));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/join-game")
    public void joinGame() {
        playersService.joinGame();
    }

    @PostMapping(value = "/new-game", consumes = "application/json")
    public void newGame(@RequestBody NewGameRequest newGameRequest) {
        playersService.createNewGame(newGameRequest);
    }

    @PostMapping(value = "/move", consumes = "application/json")
    public void newMove(@RequestBody NewMoveRequest newMoveRequest) {
        playersService.createNewMove(newMoveRequest);
    }
}
