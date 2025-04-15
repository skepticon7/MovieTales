package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Entity.MovieTracker;
import ma.movieTales.movie_service.Enums.Status;
import ma.movieTales.movie_service.Exceptions.AlreadyExistsException;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.MovieTrackerMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.MovieTrackerRepository;
import ma.movieTales.movie_service.Service.MovieTrackerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieTrackerImp  implements MovieTrackerService{

   private final MovieRepository movieRepository;
   private final MovieTrackerRepository movieTrackerRepository;

    @Override
    public MovieTrackerDTO addMovieToWatchlistOrWatchedList(String userId, Long movieId, Status status) {
        // check if the user exists in the DB (call to the user service)
        // check if the user that's making a request is the one who is currently logged in
        // check if the movie exists in the DB
        Movie movie = movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with title '" + movieId + "' already exists."));

        //check whether a tracker already exists for this one
        if (movieTrackerRepository.findMovieTrackerByUserIdAndMovieId(movieId, userId).isPresent()) {
            throw new AlreadyExistsException("MovieTracker with movie id " + movieId + " and user Id " + userId + " already exists.");
        }

        MovieTracker movieTracker = MovieTracker.builder()
                .movie(movie)
                .status(status)
                .userId(userId)
                //.user(user)
                .build();

        return MovieTrackerMapper.toDto(movieTrackerRepository.save(movieTracker));
    }

    @Override
    @Transactional
    public MovieTrackerDTO removeMovieFromWatchlistOrWatchedlist(String userId, Long movieId , Long movieTrackerId) {
        // check if the user exists in the DB (call to the user service)
        // check if the user thats making a request is the one who is currently logged in
        // check if the movie exists in the DB
        movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie with id " + movieId + " doesn't exists."));

        // check if the movie is in the watchlist or watched list
        MovieTracker movieTracker = movieTrackerRepository.findMovieTrackerById(movieTrackerId)
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
}
