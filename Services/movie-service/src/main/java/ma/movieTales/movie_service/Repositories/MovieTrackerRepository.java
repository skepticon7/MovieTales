package ma.movieTales.movie_service.Repositories;


import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
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
    List<MovieTracker> findMoviesByStatus(@Param("status") Status status , @Param("userId") String userId);

    Optional<MovieTracker> findMovieTrackerById(Long id);

    //this one removes the movie from both the watchlist and the watched list
    @Modifying
    @Query("DELETE FROM MovieTracker m WHERE m.movie.id = :movieId AND m.userId = :userId")
    void removeMovieFromTracker(@Param("movieId") Long movieId , @Param("userId") String userId);

    @Query("SELECT m FROM MovieTracker m WHERE m.movie.id = :movieId AND m.userId = :userId")
    Optional<MovieTracker> findMovieTrackerByUserIdAndMovieId(
            @Param("movieId") Long movieId,
            @Param("userId") String userId
    );

}
