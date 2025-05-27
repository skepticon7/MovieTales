package ma.movieTales.movie_service.Controller;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieStatsDTO;
import ma.movieTales.movie_service.DTO.UserTrackersAndReviewsDTO;
import ma.movieTales.movie_service.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class MovieController {

    private  MovieService movieService;

    @GetMapping("/getMovieStats")
    public ResponseEntity<MovieStatsDTO> getMovieStats(@RequestParam("movieId") Long movieId){
        MovieStatsDTO movieStats = movieService.getMovieStats(movieId);
        return new ResponseEntity<>(movieStats, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserStuff")
    public ResponseEntity<UserTrackersAndReviewsDTO> deleteUserStuff(
            @RequestParam("userId") String userId
    ){
        UserTrackersAndReviewsDTO userStuff = movieService.deleteUserStuff(userId);
        return new ResponseEntity<>(userStuff , HttpStatus.OK);
    }


}
