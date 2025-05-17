package ma.movieTales.movie_service.Controller;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Service.MovieTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MovieTrackerController {

    private final MovieTrackerService movieTrackerService;

    @PostMapping("/addToWatchListOrWatchedList")
    public ResponseEntity<MovieTrackerDTO> addToWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("status") String status
    ){
        MovieTrackerDTO movieTracked = movieTrackerService.addMovieToWatchlistOrWatchedList(userId , movieId , status);
        return new ResponseEntity<>(movieTracked , HttpStatus.CREATED);
    }



    @GetMapping("/getMovieTrackerStatus")
    public ResponseEntity<Map<String , String>> getMovieTrackerStatus(
            @RequestParam("movieId") Long movieId,
            @RequestParam("userId") String userId
    ){
        Map<String , String> status = movieTrackerService.getMovieTrackerStatus(movieId , userId);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }

    @GetMapping("/getWatchlistOrWatchedlist")
    public ResponseEntity<List<MovieTrackerDTO>> getMoviesInWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("status") String status
    ){
        List<MovieTrackerDTO> moviesTracked = movieTrackerService.getUserWatchedOrWatchlist(userId , status);
        return new ResponseEntity<>(moviesTracked , HttpStatus.OK);
    }

    @GetMapping("/getTenMoviesInWatchedList")
    public ResponseEntity<List<MovieTrackerDTO>> getTenMoviesInWatchedList(@RequestParam("userId") String userId){
        List<MovieTrackerDTO> movies = movieTrackerService.getTenMoviesInWatchedList(userId);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @DeleteMapping("/removeFromWatchListOrWatchedList")
    public ResponseEntity<MovieTrackerDTO> removeFromWatchListOrWatchedList(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId
    ){
        MovieTrackerDTO movieTracked = movieTrackerService.removeMovieFromWatchlistOrWatchedlist(userId , movieId);
        return new ResponseEntity<>(movieTracked , HttpStatus.OK);
    }

}
