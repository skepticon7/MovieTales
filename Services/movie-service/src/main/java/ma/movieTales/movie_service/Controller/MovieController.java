package ma.movieTales.movie_service.Controller;

import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@AllArgsConstructor
public class MovieController {

    private  MovieService movieService;

    @PostMapping("/addMovie")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO){
        MovieDTO movie = movieService.saveMovie(movieDTO);
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @GetMapping("/getMovie/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id){
        MovieDTO movie = movieService.getMovie(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }


}
