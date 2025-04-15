package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor , Long> {

    @Query("SELECT a FROM Actor a WHERE a.name = :name")
    Optional<Actor> getActorByName(@Param("name") String name);




}
