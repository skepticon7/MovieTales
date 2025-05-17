package ma.movieTales.movie_service.Repositories;


import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import org.springframework.data.domain.Pageable;
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


    @Query("SELECT m FROM MovieTracker m WHERE m.userId = :userId")
    List<MovieTracker> findUserMovieTrackers(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM MovieTracker m WHERE m.movie.id = :movieId AND m.userId = :userId")
    void removeMovieFromTracker(@Param("movieId") Long movieId , @Param("userId") String userId);

    @Query("SELECT m FROM MovieTracker m WHERE m.movie.id = :movieId AND m.userId = :userId")
    Optional<MovieTracker> findMovieTrackerByUserIdAndMovieId(
            @Param("movieId") Long movieId,
            @Param("userId") String userId
    );

    @Query("SELECT COUNT(*) FROM MovieTracker m WHERE m.movie.id = :movieId AND m.status = :status")
    int getMovieInWatchedOrWatchlistNumber(@Param("movieId") Long movieId , @Param("status") Status status);




    @Query("SELECT m FROM MovieTracker m WHERE m.userId = :userId AND m.status = 'WATCHED' ORDER BY m.createdAt DESC")
    List<MovieTracker> findTenMoviesInWatchedListByUserId(@Param("userId") String userId , Pageable pageable);


}
