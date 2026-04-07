package ro.mpp.repository;

import ro.mpp.domain.ShowArtist;

import java.time.LocalDate;
import java.util.List;

public interface IShowArtistRepository extends Repository<Integer, ShowArtist> {
    public List<ShowArtist> filterByDate(LocalDate date);
}
