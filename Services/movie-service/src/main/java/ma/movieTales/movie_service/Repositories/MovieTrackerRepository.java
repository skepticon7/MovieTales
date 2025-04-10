package ma.movieTales.movie_service.Repositories;


import ma.movieTales.movie_service.Entity.MovieTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieTrackerRepository extends JpaRepository<MovieTracker , Long> {

    @Query("SELECT m FROM MovieTracker m WHERE m.status = :status AND m.userId = :userId")
    List<MovieTracker> findMoviesByStatus(@Param("status") String status ,  @Param("userId") String userId);

    //this one removes the movie from both the watchlist and the watched list
    @Modifying
    @Query("DELETE FROM MovieTracker m WHERE m.movie.id = :movieId")
    void removeMovieFromTracker(@Param("movieId") Long movieId);

}
