package ma.movieTales.movie_service.ServiceImp;

import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.MovieMapper;
import ma.movieTales.movie_service.Mapper.MovieTrackerMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.MovieTrackerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieTrackerImpTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieTrackerRepository movieTrackerRepository;

    @InjectMocks
    private MovieTrackerImp movieTrackerImp;

    private Movie movie;
    private MovieTracker movieTracker;

    @BeforeEach
    void setup() {
        movie = MovieMapper.toEntity(1L);
        movieTracker = MovieTracker.builder()
                .id(100L)
                .movie(movie)
                .userId("1234")
                .status(Status.WATCHED)
                .build();

        lenient().when(movieTrackerRepository.save(any(MovieTracker.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

    }

    @Test
    void addMovieToWatchlistOrWatchedList_NewTracker() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.empty());

        MovieTrackerDTO result = movieTrackerImp.addMovieToWatchlistOrWatchedList("1234", 1L, "WATCHED");

        assertEquals("1234", result.getUserId());
        assertEquals(Status.WATCHED, result.getStatus());
        verify(movieTrackerRepository).save(any(MovieTracker.class));
    }

    @Test
    void addMovieToWatchlistOrWatchedList_ExistingTracker() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.of(movieTracker));

        MovieTrackerDTO result = movieTrackerImp.addMovieToWatchlistOrWatchedList("1234", 1L, "WATCHED");

        assertEquals("1234", result.getUserId());
        assertEquals(Status.WATCHED, result.getStatus());
        verify(movieTrackerRepository).save(any(MovieTracker.class));
    }

    @Test
    void removeMovieFromWatchlistOrWatchedlist_Success() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.of(movieTracker));

        MovieTrackerDTO result = movieTrackerImp.removeMovieFromWatchlistOrWatchedlist("1234", 1L);

        assertEquals("1234", result.getUserId());
        verify(movieTrackerRepository).removeMovieFromTracker(1L, "1234");
    }

    @Test
    void removeMovieFromWatchlistOrWatchedlist_NotFound() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(movie));
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                movieTrackerImp.removeMovieFromWatchlistOrWatchedlist("1234", 1L)
        );
    }

    @Test
    void getUserWatchedOrWatchlist() {
        when(movieTrackerRepository.findMoviesByStatus(Status.WATCHED, "1234")).thenReturn(List.of(movieTracker));

        List<MovieTrackerDTO> result = movieTrackerImp.getUserWatchedOrWatchlist("1234", "WATCHED");

        assertEquals(1, result.size());
        assertEquals("1234", result.get(0).getUserId());
    }

    @Test
    void getTenMoviesInWatchedList() {
        Pageable pageable = PageRequest.of(0, 10);
        when(movieTrackerRepository.findTenMoviesInWatchedListByUserId("1234", pageable)).thenReturn(List.of(movieTracker));

        List<MovieTrackerDTO> result = movieTrackerImp.getTenMoviesInWatchedList("1234");

        assertEquals(1, result.size());
        assertEquals("1234", result.get(0).getUserId());
    }

    @Test
    void getMovieTrackerStatus_Exists() {
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.of(movieTracker));

        Map<String, String> result = movieTrackerImp.getMovieTrackerStatus(1L, "1234");

        assertEquals("WATCHED", result.get("status"));
    }

    @Test
    void getMovieTrackerStatus_NotExists() {
        when(movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(1L, "1234")).thenReturn(Optional.empty());

        Map<String, String> result = movieTrackerImp.getMovieTrackerStatus(1L, "1234");

        assertEquals("none", result.get("status"));
    }
}
