package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

    @Query("SELECT r FROM Review r WHERE r.userId = :userId")
    List<Review> findReviewsByUserId(@Param("userId") String userId);

    Optional<Review> findReviewById(Long Id);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.id = :reviewId")
    void deleteReview(@Param("reviewId") Long reviewId);

}


