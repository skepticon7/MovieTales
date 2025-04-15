package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genres , Long> {

    @Query("SELECT g FROM Genres g WHERE g.name = :name")
    Optional<Genres> getGenreByName(@Param("name") String name);


}
