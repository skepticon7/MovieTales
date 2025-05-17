package ma.movieTales.movie_service.ServiceImp;

import ma.movieTales.movie_service.DTO.MovieStatsDTO;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.DTO.UserTrackersAndReviewsDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Entity.Review;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Mapper.MovieTrackerMapper;
import ma.movieTales.movie_service.Mapper.ReviewMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.MovieTrackerRepository;
import ma.movieTales.movie_service.Repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImpTest {

    @InjectMocks
    private MovieServiceImp movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieTrackerRepository movieTrackerRepository;

    private Movie movie;
    private Review review1, review2;
    private MovieTracker tracker1, tracker2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setVote_count(2L);
        movie.setReviews(List.of());
        movie.setVote_average(4.0f);

        review1 = new Review();
        review1.setId(1L);
        review1.setUserId("1234");
        review1.setMovie(movie);
        review1.setRating((short) 5);

        review2 = new Review();
        review2.setId(2L);
        review2.setUserId("1234");
        review2.setMovie(movie);
        review2.setRating((short) 3);

        tracker1 = new MovieTracker();
        tracker1.setUserId("1234");
        tracker1.setMovie(movie);
        tracker1.setStatus(Status.WATCHED);

        tracker2 = new MovieTracker();
        tracker2.setUserId("1234");
        tracker2.setMovie(movie);
        tracker2.setStatus(Status.WATCHLIST);
    }

    @Test
    void testGetMovieStats_MovieExists() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(1L, Status.WATCHED)).thenReturn(5);
        when(movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(1L, Status.WATCHLIST)).thenReturn(3);

        MovieStatsDTO stats = movieService.getMovieStats(1L);

        assertNotNull(stats);
        assertEquals(5, stats.getWatched());
        assertEquals(3, stats.getWatchlist());
        assertEquals(2L, stats.getVoteCount());
        assertEquals(4.0f, stats.getAverageRating());
    }

    @Test
    void testGetMovieStats_MovieDoesNotExist() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.empty());

        MovieStatsDTO stats = movieService.getMovieStats(1L);

        assertNotNull(stats);
        assertEquals(0, stats.getWatched());
        assertEquals(0, stats.getWatchlist());
        assertEquals(0L, stats.getVoteCount());
        assertEquals(0.0f, stats.getAverageRating());
    }

    @Test
    void testDeleteUserStuff() {
        when(movieTrackerRepository.findUserMovieTrackers("1234")).thenReturn(List.of(tracker1, tracker2));
        when(reviewRepository.findReviewsByUserId("1234")).thenReturn(List.of(review1, review2));
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(reviewRepository.getSumOfMovieReviews(1L)).thenReturn(8);

        UserTrackersAndReviewsDTO result = movieService.deleteUserStuff("1234");

        verify(reviewRepository, times(1)).deleteAllById(List.of(1L, 2L));
        verify(movieTrackerRepository, times(1)).deleteAll(List.of(tracker1, tracker2));

        assertEquals(0L, movie.getVote_count());
        assertEquals(0.0f, movie.getVote_average());

        assertEquals(2, result.getReviews().size());
        assertEquals(2, result.getMovieTrackers().size());

        List<ReviewDTO> expectedReviews = List.of(ReviewMapper.toDto(review1), ReviewMapper.toDto(review2));
        List<MovieTrackerDTO> expectedTrackers = List.of(MovieTrackerMapper.toDto(tracker1), MovieTrackerMapper.toDto(tracker2));

        assertEquals(expectedReviews, result.getReviews());
        assertEquals(expectedTrackers, result.getMovieTrackers());
    }
}
