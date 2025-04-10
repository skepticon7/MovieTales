package ma.movieTales.movie_service.Service;

import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {


    MovieDTO getMovie(Long id);

    //save movie from TDBM in our DB
    MovieDTO saveMovie(MovieDTO movieDTO);

    // get the movies rated by the user
    List<MovieDTO> getUserRatedList(String userId);


}
