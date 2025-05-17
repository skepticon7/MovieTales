package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.*;
import ma.movieTales.movie_service.Entity.*;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Mapper.*;
import ma.movieTales.movie_service.Repositories.*;
import ma.movieTales.movie_service.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieServiceImp implements MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final MovieTrackerRepository movieTrackerRepository;



    @Override
    public MovieStatsDTO getMovieStats(Long movieId) {
        Optional<Movie> movieOptional = movieRepository.findMovieById(movieId);
        if (movieOptional.isEmpty()) {
            return MovieStatsDTO.builder()
                    .watched(0)
                    .watchlist(0)
                    .voteCount(0L)
                    .averageRating(0.0f)
                    .build();
        }

        Movie movie = movieOptional.get();
        int watched = movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(movieId, Status.WATCHED);
        int watchlist = movieTrackerRepository.getMovieInWatchedOrWatchlistNumber(movieId, Status.WATCHLIST);
        Long voteCount = movie.getVote_count();
        Float averageRating = movie.getVote_average();

        return MovieStatsDTO.builder()
                .watched(watched)
                .watchlist(watchlist)
                .voteCount(voteCount)
                .averageRating(averageRating)
                .build();
    }

    @Override
    @Transactional
    public UserTrackersAndReviewsDTO deleteUserStuff(String userId) {
        // Fetch all trackers and reviews
        List<MovieTracker> userMovieTrackers = movieTrackerRepository.findUserMovieTrackers(userId);
        List<Review> userReviews = reviewRepository.findReviewsByUserId(userId);

        // Group reviews by movie and update stats once
        Map<Long, List<Review>> reviewsByMovie = userReviews.stream()
                .collect(Collectors.groupingBy(review -> review.getMovie().getId()));

        reviewsByMovie.forEach((movieId, reviews) -> {
            Optional<Movie> movieOptional = movieRepository.findMovieById(movieId);
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                // Get the original count and sum
                Integer originalReviewSum = reviewRepository.getSumOfMovieReviews(movieId);
                int originalVoteCount = Math.toIntExact(movie.getVote_count());

                // Compute the new sum and vote count
                int reviewsToRemove = reviews.size();
                Integer newReviewSum = originalReviewSum - reviews.stream().mapToInt(Review::getRating).sum();
                int newVoteCount = Math.max(originalVoteCount - reviewsToRemove, 0);

                // Update the movie
                movie.setVote_count((long) newVoteCount);
                float newAverage = newVoteCount > 0 ? (newReviewSum / (float) newVoteCount) : 0.0f;
                movie.setVote_average(newAverage);

                movieRepository.save(movie);
            }
        });

        // Bulk delete reviews
        List<Long> reviewIds = userReviews.stream()
                .map(Review::getId)
                .toList();
        reviewRepository.deleteAllById(reviewIds);

        // Delete all trackers
        movieTrackerRepository.deleteAll(userMovieTrackers);

        // Convert to DTOs
        List<ReviewDTO> reviews = userReviews.stream()
                .map(ReviewMapper::toDto)
                .toList();

        List<MovieTrackerDTO> movieTrackers = userMovieTrackers.stream()
                .map(MovieTrackerMapper::toDto)
                .toList();

        return new UserTrackersAndReviewsDTO(reviews, movieTrackers);
    }




}
