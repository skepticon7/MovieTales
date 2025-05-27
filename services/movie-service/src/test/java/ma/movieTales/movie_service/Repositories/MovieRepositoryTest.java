package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("findMovieById should return movie when movie exists")
     void testFindMovieById_WhenMovieExists() {
        // Given
        Movie movie = new Movie();
        movie.setId(1L);
        movie = movieRepository.save(movie);

        Optional<Movie> foundMovie = movieRepository.findMovieById(movie.getId());

        assertTrue(foundMovie.isPresent());
        assertEquals(1L, foundMovie.get().getId());
        assertEquals(movie.getId(), foundMovie.get().getId());
    }

    @Test
    @DisplayName("findMovieById should return empty when movie does not exist")
    void testFindMovieById_WhenMovieDoesNotExist() {
        Optional<Movie> foundMovie = movieRepository.findMovieById(999L);

        assertFalse(foundMovie.isPresent());
    }
}