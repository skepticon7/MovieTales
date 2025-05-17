package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
//import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Exceptions.AlreadyExistsException;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.MovieMapper;
import ma.movieTales.movie_service.Mapper.MovieTrackerMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.MovieTrackerRepository;
import ma.movieTales.movie_service.Service.MovieTrackerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieTrackerImp implements MovieTrackerService{

   private final MovieRepository movieRepository;
   private final MovieTrackerRepository movieTrackerRepository;

    @Override
    @Transactional
    public MovieTrackerDTO addMovieToWatchlistOrWatchedList(String userId, Long movieId, String status) {
        // Check if the movie exists in the DB, if not, create and save a new movie
        Movie movie = movieRepository.findMovieById(movieId)
                .orElseGet(() -> {
                    Movie newMovie = MovieMapper.toEntity(movieId);
                    return movieRepository.save(newMovie);
                });

        // Check whether a tracker already exists for this movie
        Optional<MovieTracker> movieTrackerOptional = movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movieId, userId);
        if (movieTrackerOptional.isPresent()) {
            Status newStatus = Status.valueOf(status.toUpperCase());
            MovieTracker movieTracker = movieTrackerOptional.get();
            movieTracker.setStatus(newStatus);
            MovieTracker savedTracker = movieTrackerRepository.save(movieTracker);
            return MovieTrackerMapper.toDto(savedTracker);
        }

        Status newStatus = Status.valueOf(status.toUpperCase());

        MovieTracker movieTracker = MovieTracker.builder()
                .movie(movie)
                .status(newStatus)
                .userId(userId)
                .build();

        return MovieTrackerMapper.toDto(movieTrackerRepository.save(movieTracker));
    }

    @Override
    @Transactional
    public MovieTrackerDTO removeMovieFromWatchlistOrWatchedlist(String userId, Long movieId) {
        // check if the user exists in the DB (call to the user service)
        // check if the user thats making a request is the one who is currently logged in
        // check if the movie exists in the DB
        movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id " + movieId + " doesn't exists."));

        // check if the movie is in the watchlist or watched list
        MovieTracker movieTracker = movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movieId , userId)
                .orElseThrow(() -> new NotFoundException("MovieTracker with movie id " + movieId + "and user Id " + userId + " doesn't exists."));

        movieTrackerRepository.removeMovieFromTracker(movieId , userId);

        return MovieTrackerMapper.toDto(movieTracker);

    }



    @Override
    public List<MovieTrackerDTO> getUserWatchedOrWatchlist(String userId , String status) {
        //make sure user making the call is logged in
        // check if the user exists in the DB (call to the user service)
        Status statusEnum = Status.valueOf(status.toUpperCase());
        return movieTrackerRepository.findMoviesByStatus(statusEnum , userId).stream()
                .map(MovieTrackerMapper::toDto)
                .toList();

    }


    @Override
    public List<MovieTrackerDTO> getTenMoviesInWatchedList(String userId) {
        Pageable pageable = PageRequest.of(0 , 10);
        return movieTrackerRepository.findTenMoviesInWatchedListByUserId(userId , pageable).stream()
                .map(MovieTrackerMapper::toDto)
                .toList();
    }

    @Override
    public Map<String, String> getMovieTrackerStatus(Long movieId , String userId) {
        return movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movieId , userId)
                .map(movieTracker -> Map.of("status" , movieTracker.getStatus().toString()))
                .orElse(Map.of("status" , "none"));
    }
}
