package ma.movieTales.movie_service.Service;

//import ma.movieTales.movie_service.DTO.ActorMoviesDTO;
//import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.DTO.MovieStatsDTO;
import ma.movieTales.movie_service.DTO.UserTrackersAndReviewsDTO;
import ma.movieTales.movie_service.Entity.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.List;
import java.util.Optional;

public interface MovieService {



    MovieStatsDTO getMovieStats(Long movieId);

    UserTrackersAndReviewsDTO deleteUserStuff(String userId);



}
