package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie , Long> {

    Optional<Movie> findMovieById(Long id);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%' , :title))")
    List<Movie> getMoviesByTitle(@Param("title") String title);

    @Query("SELECT m FROM Movie m WHERE m.title = :title")
    Optional<Movie> getMovieByTitle(@Param("title") String title);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    List<Movie> getMoviesDoneByActor(@Param("actorId") Long actorId);

    @Query("SELECT m FROM Movie m JOIN m.production_companies pc WHERE pc.id = :productionCompanyId")
    List<Movie> findMoviesDoneByProductionCompany(@Param("productionCompanyId") Long productionCompanyId);

}
