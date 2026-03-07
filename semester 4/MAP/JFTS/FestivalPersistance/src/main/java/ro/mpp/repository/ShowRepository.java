package ro.mpp.repository;

import ro.mpp.domain.Artist;
import ro.mpp.domain.Show;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends Repository<Integer, Show> {
    public List<Show> findByPerformerAndDate(Artist performer, LocalDateTime date);
}
