package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.Configuration.JwtTokenProvider;
import ma.movieTales.movie_service.Controller.ReviewController;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.Review;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.MovieMapper;
import ma.movieTales.movie_service.Mapper.ReviewMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.ReviewRepository;
import ma.movieTales.movie_service.Service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    @Transactional
    public ReviewDTO addReviewToMovie(String userId, Long movieId, ReviewDTO reviewDTO) {
        //make sure the user who is making this request is the one who is currently loggedin
        //make sure the user exists in the DB(call to user-service)
        //make sure the movie exists

        Movie movie = movieRepository.findMovieById(movieId)
                .orElseGet(() -> {
                    Movie newMovie = MovieMapper.toEntity(movieId);
                    return movieRepository.save(newMovie);
                });

        movie.setVote_count(movie.getVote_count() + 1);

        //getting the sum of all reviews
        Integer reviewSum = reviewRepository.getSumOfMovieReviews(movieId);

        Float newAverage = (reviewSum + reviewDTO.getRating()) / (float) movie.getVote_count();
        movie.setVote_average(newAverage);
        movie = movieRepository.save(movie);


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
        Movie movie = movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id: " + movieId + " not found"));

        //make sure the review exists
        Review review = reviewRepository.findReviewById(reviewId)
                        .orElseThrow(() -> new NotFoundException("Review with id: " + reviewId + " not found"));

        //getting the sum of all reviews
        Integer reviewSum = reviewRepository.getSumOfMovieReviews(movieId);

        reviewSum -= review.getRating();
        Float newAverage = (reviewSum + reviewDTO.getRating()) / (float) movie.getVote_count();
        movie.setVote_average(newAverage);
        movieRepository.save(movie);
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
        Movie movie = movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id: " + movieId + " not found"));

        //make sure the review exists
        Review review = reviewRepository.findReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id: " + reviewId + " not found"));


        //getting the sum of all reviews
        Integer reviewSum = reviewRepository.getSumOfMovieReviews(movieId);

        reviewSum -= review.getRating();
        movie.setVote_count(movie.getVote_count() - 1);
        Float newAverage = (reviewSum  / (float) movie.getVote_count());
        movie.setVote_average(newAverage);
        movieRepository.save(movie);

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


    @Override
    public List<ReviewDTO> getMovieReviews(Long movieId) {

        List<ReviewDTO> reviewsDTO = reviewRepository.findReviewsByMovie(movieId).stream().map(review -> {
            //get user in accordance to each review
            ReviewDTO dto = ReviewMapper.toDto(review);
            System.out.println(review.getUserId());
            String url = "http://localhost:5050/api/users/getUserSettings?userId="+review.getUserId();
            String token = jwtTokenProvider.getTokenFromRequest();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization" , "Bearer "+token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    UserDTO userDTO = new UserDTO();
                    Map<String, Object> responseBody = response.getBody();
                    Object userDTOData = responseBody.get("UserDTO");
                    modelMapper.map(userDTOData , userDTO);
                    dto.setUserDTO(userDTO);
                }
            } catch (Exception e) {
                System.out.println("Error fetching user data for userId "  + ": " + e.getMessage());
            }
            return dto;
        }).toList();

        return reviewsDTO;
    }


}
