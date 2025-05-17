package ma.movieTales.movie_service.ServiceImp;

import ma.movieTales.movie_service.Configuration.JwtTokenProvider;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.Review;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.ReviewRepository;
import ma.movieTales.movie_service.ServiceImp.ReviewServiceImp;
import ma.movieTales.movie_service.Mapper.ReviewMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImpTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private ReviewServiceImp reviewService;

    private Movie movie;
    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setup() {
        movie = new Movie();
        movie.setId(1L);
        movie.setVote_count(5L);
        movie.setVote_average(4.0f);

        review = new Review();
        review.setId(10L);
        review.setRating(Short.parseShort("3"));
        review.setUserId("user1");
        review.setMovie(movie);

        reviewDTO = new ReviewDTO();
        reviewDTO.setRating(Short.parseShort("5"));
    }

    @Test
    void addReviewToMovie_ShouldAddReviewAndUpdateMovie() {
        when(movieRepository.findMovieById(anyLong())).thenReturn(Optional.of(movie));
        when(reviewRepository.getSumOfMovieReviews(anyLong())).thenReturn(20);
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(movieRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewDTO result = reviewService.addReviewToMovie("user1", 1L, reviewDTO);

        assertNotNull(result);
        verify(movieRepository).save(any());
        verify(reviewRepository).save(any());
        assertEquals(6, movie.getVote_count()); // vote_count incremented
        // vote_average updated as (20 + 5) / 6 = 25/6 ~4.1667
        assertEquals(25f / 6, movie.getVote_average());
    }

    @Test
    void updateReview_ShouldUpdateReviewAndMovieAverage() {
        when(movieRepository.findMovieById(anyLong())).thenReturn(Optional.of(movie));
        when(reviewRepository.findReviewById(anyLong())).thenReturn(Optional.of(review));
        when(reviewRepository.getSumOfMovieReviews(anyLong())).thenReturn(20);
        when(movieRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(modelMapper.getConfiguration()).thenReturn(mock(org.modelmapper.config.Configuration.class));

        ReviewDTO result = reviewService.updateReview("user1", 1L, reviewDTO, 10L);

        assertNotNull(result);
        verify(movieRepository).save(any());
        verify(reviewRepository).save(any());
        // Expected average: (20 - 3 + 5) / 5 = 22 / 5 = 4.4
        assertEquals(4.4f, movie.getVote_average());
    }

    @Test
    void deleteReview_ShouldDeleteReviewAndUpdateMovieAverage() {
        when(movieRepository.findMovieById(anyLong())).thenReturn(Optional.of(movie));
        when(reviewRepository.findReviewById(anyLong())).thenReturn(Optional.of(review));
        when(reviewRepository.getSumOfMovieReviews(anyLong())).thenReturn(20);
        when(movieRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewDTO deletedReviewDTO = reviewService.deleteReview("user1", 1L, 10L);

        assertNotNull(deletedReviewDTO);
        verify(movieRepository).save(any());
        verify(reviewRepository).deleteReview(10L);
        assertEquals(4, movie.getVote_count());
        assertEquals(4.25f, movie.getVote_average());
    }

    @Test
    void getUserReviews_ShouldReturnListOfReviewDTO() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findReviewsByUserId("user1")).thenReturn(reviews);

        List<ReviewDTO> dtos = reviewService.getUserReviews("user1");

        assertEquals(1, dtos.size());
        verify(reviewRepository).findReviewsByUserId("user1");
    }

    @Test
    void updateReview_WhenMovieNotFound_ShouldThrow() {
        when(movieRepository.findMovieById(anyLong())).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                reviewService.updateReview("user1", 99L, reviewDTO, 10L));
        assertTrue(ex.getMessage().contains("Movie with id"));
    }

    @Test
    void updateReview_WhenReviewNotFound_ShouldThrow() {
        when(movieRepository.findMovieById(anyLong())).thenReturn(Optional.of(movie));
        when(reviewRepository.findReviewById(anyLong())).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                reviewService.updateReview("user1", 1L, reviewDTO, 99L));
        assertTrue(ex.getMessage().contains("Review with id"));
    }

}
