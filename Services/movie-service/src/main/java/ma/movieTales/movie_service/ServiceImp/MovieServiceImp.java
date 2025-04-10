package ma.movieTales.movie_service.ServiceImp;

import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Entity.Movie;
import ma.movieTales.movie_service.Mapper.MovieMapper;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieServiceImp implements MovieService {

    private final MovieRepository movieRepository;


    @Override
    public MovieDTO getMovie(Long id) {
        return MovieMapper.toDto(movieRepository.getMovieById(id));
    }

    @Override
    public MovieDTO saveMovie(MovieDTO movieDTO) {
        // save actors
        // save genres
        // save production companies
        return MovieMapper.toDto(movieRepository.save(MovieMapper.toEntity(movieDTO)));
    }

    @Override
    public List<MovieDTO> getUserRatedList(String userId) {
        return List.of();
    }
}
