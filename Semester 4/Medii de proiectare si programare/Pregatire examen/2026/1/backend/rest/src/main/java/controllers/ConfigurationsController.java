package controllers;

import dtos.CreateConfigurationRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import services.ConfigurationsService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configurations")
public class ConfigurationsController {

    private final ConfigurationsService configurationsService;

    private final SimpMessagingTemplate messagingTemplate;

    private final Logger logger = LogManager.getLogger();

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(configurationsService.findAll());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity save(@RequestBody CreateConfigurationRequest request) {
        try {
            var result = configurationsService.save(request);
//            messagingTemplate.convertAndSend("/topic/updates", Map.of(
//                    "id", result
//            ));
//            messagingTemplate.convertAndSendToUser(
//                    "player1",
//                    "/queue/private",
//                    Map.of(
//                            "type", "CONFIGURATION_CREATED",
//                            "id", result
//                    )
//            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid data");
        }
    }
}
