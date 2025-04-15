package ma.movieTales.movie_service.Service;

import com.netflix.appinfo.ApplicationInfoManager;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    //adding a review to a movie
    ReviewDTO addReviewToMovie(String userId , Long movieId , ReviewDTO reviewDTO);

    //updating a review
    ReviewDTO updateReview(String userId , Long movieId , ReviewDTO reviewDTO , Long reviewId);

    //deleting a review
    ReviewDTO deleteReview(String userId , Long movieId , Long reviewId);

    //getting the user reviews
    List<ReviewDTO> getUserReviews(String userId);

}
