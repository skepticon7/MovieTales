package ma.movieTales.movie_service.Service;

import com.netflix.appinfo.ApplicationInfoManager;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    ReviewDTO addReviewToMovie(String userId , Long movieId , ReviewDTO reviewDTO);

    ReviewDTO updateReview(String userId , Long movieId , ReviewDTO reviewDTO , Long reviewId);

    ReviewDTO deleteReview(String userId , Long movieId , Long reviewId);

    List<ReviewDTO> getUserReviews(String userId);

    List<ReviewDTO> getMovieReviews(Long movieId);
}
