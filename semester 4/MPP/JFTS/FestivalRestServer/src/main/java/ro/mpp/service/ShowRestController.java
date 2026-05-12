package ro.mpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.exceptions.TicketModifier;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("fts/show")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShowRestController {

    private final IFestivalService festivalService;

    @GetMapping
    public ResponseEntity<List<ShowArtist>> getAll() {
        return ResponseEntity.ok(festivalService.findAll());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowArtist>> getByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date); // expects ISO format: 2025-04-28
        return ResponseEntity.ok(festivalService.findByDate(localDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getById(@PathVariable Integer id) {
        return festivalService.findShowById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Show show) {
        try {
            return festivalService.saveShow(show)
                    .map(s -> ResponseEntity.status(HttpStatus.CREATED).body((Object) s))
                    .orElse(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            e.printStackTrace(); // ← add this temporarily
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Show> update(@PathVariable Integer id, @RequestBody Show show) {
        return festivalService.updateShow(id, show)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = festivalService.deleteShow(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/ticket")
    public ResponseEntity<?> sellTicket(
            @PathVariable Integer id,
            @RequestParam String buyerName,
            @RequestParam int seats
    ) {
        return festivalService.findShowById(id).map(show -> {
            try {
                return festivalService.sellTicket(show, buyerName, seats)
                        .map(ticket -> ResponseEntity.status(HttpStatus.CREATED).body((Object) ticket))
                        .orElse(ResponseEntity.badRequest().body("Could not sell ticket"));
            } catch (TicketModifier e) {
                return ResponseEntity.badRequest().body((Object) e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/ticket/{ticketId}")
    public ResponseEntity<?> modifyTicket(@PathVariable int ticketId, @RequestParam int seats) {
        try {
            return festivalService.modifyTicket(ticketId, seats) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (TicketModifier e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
