package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId")
    List<Review> findReviewsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT r FROM Review r WHERE r.userId = :userId")
    List<Review> findReviewsByUserId(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.movie.id = :movieId")
    void deleteReview(@Param("movieId") Long movieId);

}


