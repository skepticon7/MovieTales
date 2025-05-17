package ma.movieTales.movie_service.Service;

//import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieTrackerService {

    MovieTrackerDTO addMovieToWatchlistOrWatchedList(String userId, Long movieId, String status);

    MovieTrackerDTO removeMovieFromWatchlistOrWatchedlist(String userId , Long movieId);

    List<MovieTrackerDTO> getUserWatchedOrWatchlist(String userId , String status);


    List<MovieTrackerDTO> getTenMoviesInWatchedList(String userId);

    Map<String, String> getMovieTrackerStatus(Long movieId , String userId);
}
