package ma.movieTales.movie_service.Service;

import com.netflix.appinfo.ApplicationInfoManager;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    //adding a review to a movie
    Optional<Review> addReviewToMovie(String userId , Long movieId , ReviewDTO reviewDTO);

    //updating a review
    Optional<Review> updateReview(String userId , Long movieId , ReviewDTO reviewDTO);

    //deleting a review
    Optional<Review> deleteReview(String userId , Long movieId);

    //getting the movie reviews
    List<Review> getMovieReviews(Long movieId);

    //getting the user reviews
    List<Review> getUserReviews(String userId);

}
