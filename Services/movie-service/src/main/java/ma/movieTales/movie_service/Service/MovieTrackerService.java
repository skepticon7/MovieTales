package ma.movieTales.movie_service.Service;

import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public interface MovieTrackerService {

    //adding a movie to the watchlist or watchedList
    MovieTrackerDTO addMovieToWatchlistOrWatchedList(String userId, Long movieId, Status status);

    //removing a movie from the watchlist or watched list
    MovieTrackerDTO removeMovieFromWatchlistOrWatchedlist(String userId , Long movieId , Long movieTrackerId);

    // get the movies that the user has watched or in watchlist
    List<MovieTrackerDTO> getUserWatchedOrWatchlist(String userId , String status);


}
