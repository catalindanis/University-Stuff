package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.dto.ShowRequestDTO;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shows")
public class ShowsController {

    private final ShowsRepository showsRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public Long save(@RequestBody ShowRequestDTO show) {
        Show toSave = new Show(null, show.artistName(), show.date(), show.location(), show.remainingSeats());
        Long id = showsRepository.save(toSave);
        messagingTemplate.convertAndSend("/topic/shows", "refresh");
        return id;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        showsRepository.delete(id);
        messagingTemplate.convertAndSend("/topic/shows", "refresh");
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") long id, @RequestBody ShowRequestDTO showRequest) {
        Show show = new Show(id, showRequest.artistName(), showRequest.date(), showRequest.location(), showRequest.remainingSeats());
        showsRepository.update(id, show);
        messagingTemplate.convertAndSend("/topic/shows", "refresh");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long id) {
        Show show = showsRepository.findOne(id);

        if(show == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Show not found");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(show);
    }

    @GetMapping
    public Iterable<Show> findAll(
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer numberOfSeats
    ) {
        ShowFilter filter = new ShowFilter(artistName, date, location, numberOfSeats);
        return showsRepository.findAll(filter);
    }
}
