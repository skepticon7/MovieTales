package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie , Long> {

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%' , :title))")
    List<Movie> getMovieByTitle(@Param("title") String title);

    @Query("SELECT m FROM Movie m WHERE m.id = :id")
    Movie getMovieById(@Param("id") Long id);

}
