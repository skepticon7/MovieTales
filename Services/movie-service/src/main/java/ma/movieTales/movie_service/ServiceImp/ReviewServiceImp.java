package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.Review;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.ReviewMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.ReviewRepository;
import ma.movieTales.movie_service.Service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ReviewDTO addReviewToMovie(String userId, Long movieId, ReviewDTO reviewDTO) {
        //make sure the user who is making this request is the one who is currently loggedin
        //make sure the user exists in the DB(call to user-service)
        //make sure the movie exists

        Movie movie = movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id: " + movieId + " not found"));

        Review reviewEntity = ReviewMapper.toEntity(reviewDTO);
        reviewEntity.setUserId(userId);
        reviewEntity.setMovie(movie);

        Review newReview = reviewRepository.save(reviewEntity);

        return ReviewMapper.toDto(newReview);
    }

    @Override
    public ReviewDTO updateReview(String userId, Long movieId, ReviewDTO reviewDTO , Long reviewId) {
        //make sure the user who is making this request is the one who is currently loggedin
        //make sure the user exists in the DB(call to user-service)
        //make sure the movie exists
        movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id: " + movieId + " not found"));

        //make sure the review exists
        Review review = reviewRepository.findReviewById(reviewId)
                        .orElseThrow(() -> new NotFoundException("Review with id: " + reviewId + " not found"));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(reviewDTO, review);
        return ReviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewDTO deleteReview(String userId, Long movieId , Long reviewId) {
        //make sure the user who is making this request is the one who is currently loggedin
        //make sure the user exists in the DB(call to user-service)
        //make sure the movie exists
        movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id: " + movieId + " not found"));

        //make sure the review exists
        Review review = reviewRepository.findReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id: " + reviewId + " not found"));

        reviewRepository.deleteReview(reviewId);
        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getUserReviews(String userId) {
        //make sure the user who is making this request is the one who is currently loggedin
        //make sure the user exists in the DB(call to user-service)

        return reviewRepository.findReviewsByUserId(userId).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }


}
