package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.dto.ShowRequestDTO;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsRepository;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shows")
public class ShowsController {

    private final ShowsRepository showsRepository;

    @PostMapping
    public Long save(@RequestBody ShowRequestDTO show) {
        return showsRepository.save(new Show(null, show.artistName(), show.date(), show.location(), show.remainingSeats()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        showsRepository.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") long id, @RequestBody ShowRequestDTO show) {
        showsRepository.update(id, new Show(null, show.artistName(), show.date(), show.location(), show.remainingSeats()));
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
