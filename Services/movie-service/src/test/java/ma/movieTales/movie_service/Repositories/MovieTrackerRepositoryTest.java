package ma.movieTales.movie_service.Repositories;

import jakarta.transaction.Transactional;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
class MovieTrackerRepositoryTest {

    @Autowired
    private MovieTrackerRepository movieTrackerRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setup() {
        movie1 = new Movie();
        movie1.setId(2L);
        movie1 = movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setId(3L);
        movie2 = movieRepository.save(movie2);

        movieTrackerRepository.save(createMovieTracker("1234", movie1, Status.WATCHED));
        movieTrackerRepository.save(createMovieTracker("1234", movie2, Status.WATCHLIST));
        movieTrackerRepository.save(createMovieTracker("1223", movie1, Status.WATCHED));
    }

    private MovieTracker createMovieTracker(String userId, Movie movie, Status status) {
        MovieTracker mt = new MovieTracker();
        mt.setUserId(userId);
        mt.setMovie(movie);
        mt.setStatus(status);
        mt.setCreatedAt(LocalDateTime.now());
        return mt;
    }

    @Test
    void testFindMoviesByStatus() {
        List<MovieTracker> watched = movieTrackerRepository.findMoviesByStatus(Status.WATCHED, "1234");
        assertEquals(1, watched.size());
        assertEquals(Status.WATCHED, watched.get(0).getStatus());
        assertEquals("1234", watched.get(0).getUserId());
    }

    @Test
    void testFindUserMovieTrackers() {
        List<MovieTracker> trackers = movieTrackerRepository.findUserMovieTrackers("1234");
        assertEquals(2, trackers.size());
        assertTrue(trackers.stream().allMatch(mt -> mt.getUserId().equals("1234")));
    }

    @Test
    void testRemoveMovieFromTracker() {
        List<MovieTracker> before = movieTrackerRepository.findUserMovieTrackers("1234");
        assertFalse(before.isEmpty());

        movieTrackerRepository.removeMovieFromTracker(movie1.getId(), "1234");

        List<MovieTracker> after = movieTrackerRepository.findUserMovieTrackers("1234");
        assertEquals(before.size() - 1, after.size());

        Optional<MovieTracker> deleted = movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movie1.getId(), "1234");
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testFindMovieTrackerByUserIdAndMovieId() {
        Optional<MovieTracker> tracker = movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movie1.getId(), "1234");
        assertTrue(tracker.isPresent());
        assertEquals("1234", tracker.get().getUserId());
        assertEquals(movie1.getId(), tracker.get().getMovie().getId());
    }

    @Test
    void testGetMovieInWatchedOrWatchlistNumber() {
        int watchedCount = movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(movie1.getId(), Status.WATCHED);
        assertEquals(2, watchedCount);

        int watchlistCount = movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(movie2.getId(), Status.WATCHLIST);
        assertEquals(1, watchlistCount);
    }

    @Test
    void testFindTenMoviesInWatchedListByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<MovieTracker> watchedList = movieTrackerRepository.findTenMoviesInWatchedListByUserId("1234", pageable);

        assertFalse(watchedList.isEmpty());
        assertTrue(watchedList.size() <= 10);
        assertTrue(watchedList.stream().allMatch(mt -> mt.getUserId().equals("1234")));
        assertTrue(watchedList.stream().allMatch(mt -> mt.getStatus() == Status.WATCHED));
    }
}
