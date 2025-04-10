package ma.movieTales.movie_service.Service;

import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MovieTrackerService {

    //adding a movie to the watchlist
    Optional<MovieTracker> addMovieToWatchlist(String userId , Long movieId);

    //removing a movie from the watchlist
    Optional<MovieTracker> removeMovieFromWatchlist(String userId , Long movieId);

    //adding a movie to watched list
    Optional<MovieTracker> addMovieToWatchedList(String userId , Long movieId);

    //removing a movie from the watched list
    Optional<MovieTracker> removeMovieFromWatchedList(String userId , Long movieId);

    //marking a movie as watched
    Optional<MovieTracker> markMovieAsWatched(String userId , Long movieId);

    // get the movies that the user has watched
    List<Movie> getUserWatchedList(String userId);

    // get the movies that the user has added to his watchlist
    List<Movie> getUserWatchList(String userId);


}
