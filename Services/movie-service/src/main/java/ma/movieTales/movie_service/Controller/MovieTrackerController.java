package ma.movieTales.movie_service.Controller;

import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.MovieTrackerRepository;
import ma.movieTales.movie_service.Service.MovieTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MovieTrackerController {

    private final MovieTrackerService movieTrackerService;

    //add to watchlist or the watchedList
    @PostMapping("/addToWatchListOrWatchedList")
    public ResponseEntity<MovieTrackerDTO> addToWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("status") Status status
    ){
        MovieTrackerDTO movieTracked = movieTrackerService.addMovieToWatchlistOrWatchedList(userId , movieId , status);
        return new ResponseEntity<>(movieTracked , HttpStatus.CREATED);
    }

    //get movies in watchlist;
    @GetMapping("/getWatchlistOrWatchedlist")
    public ResponseEntity<List<MovieTrackerDTO>> getMoviesInWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("status") String status
    ){
        List<MovieTrackerDTO> moviesTracked = movieTrackerService.getUserWatchedOrWatchlist(userId , status);
        return new ResponseEntity<>(moviesTracked , HttpStatus.OK);
    }

    //remove movies from watchedList or watchlist;
    @DeleteMapping("/removeFromWatchListOrWatchedList")
    public ResponseEntity<MovieTrackerDTO> removeFromWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("movieTrackerId") Long movieTrackerId
    ){
        MovieTrackerDTO movieTracked = movieTrackerService.removeMovieFromWatchlistOrWatchedlist(userId , movieId , movieTrackerId);
        return new ResponseEntity<>(movieTracked , HttpStatus.OK);
    }

}
