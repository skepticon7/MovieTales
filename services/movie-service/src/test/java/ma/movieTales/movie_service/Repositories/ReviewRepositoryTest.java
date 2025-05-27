package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setup() {
        // Create and save movies for foreign key reference
        movie1 = new Movie();
        movie1.setId(2L);
        movie1 = movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setId(3L);
        movie2 = movieRepository.save(movie2);

        // Save some reviews
        reviewRepository.save(createReview("user1", movie1, (short) 4, "Great movie!"));
        reviewRepository.save(createReview("user1", movie2, (short) 5, "Amazing!"));
        reviewRepository.save(createReview("user2", movie1, (short) 3, "Good."));
    }

    private Review createReview(String userId, Movie movie, Short rating, String reviewText) {
        Review review = new Review();
        review.setUserId(userId);
        review.setMovie(movie);
        review.setRating(rating);
        review.setReview(reviewText);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        return review;
    }

    @Test
    void testFindReviewsByUserId() {
        List<Review> reviews = reviewRepository.findReviewsByUserId("user1");
        assertEquals(2, reviews.size());
        assertTrue(reviews.stream().allMatch(r -> r.getUserId().equals("user1")));
    }

    @Test
    void testFindReviewById() {
        Review savedReview = reviewRepository.findReviewsByUserId("user1").get(0);
        Optional<Review> found = reviewRepository.findReviewById(savedReview.getId());
        assertTrue(found.isPresent());
        assertEquals(savedReview.getId(), found.get().getId());
    }

    @Test
    void testFindReviewsByMovie() {
        List<Review> movie1Reviews = reviewRepository.findReviewsByMovie(movie1.getId());
        assertEquals(2, movie1Reviews.size());
        assertTrue(movie1Reviews.stream().allMatch(r -> r.getMovie().getId().equals(movie1.getId())));
    }

    @Test
    void testDeleteReview() {
        Review reviewToDelete = reviewRepository.findReviewsByUserId("user1").get(0);
        Long reviewId = reviewToDelete.getId();

        reviewRepository.deleteReview(reviewId);

        Optional<Review> deleted = reviewRepository.findReviewById(reviewId);
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testGetSumOfMovieReviews() {
        Integer sumRatingsMovie1 = reviewRepository.getSumOfMovieReviews(movie1.getId());
        assertEquals(7, sumRatingsMovie1);  // 4 + 3

        Integer sumRatingsMovie2 = reviewRepository.getSumOfMovieReviews(movie2.getId());
        assertEquals(5, sumRatingsMovie2);
    }
}
