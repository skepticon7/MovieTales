package ma.movieTales.movie_service.ServiceImp;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Entity.*;
import ma.movieTales.movie_service.Exceptions.AlreadyExistsException;
import ma.movieTales.movie_service.Exceptions.NotFoundException;
import ma.movieTales.movie_service.Mapper.ActorMapper;
import ma.movieTales.movie_service.Mapper.GenreMapper;
import ma.movieTales.movie_service.Mapper.MovieMapper;
import ma.movieTales.movie_service.Mapper.ProductionCompanyMapper;
import ma.movieTales.movie_service.MovieServiceApplication;
import ma.movieTales.movie_service.Repositories.ActorRepository;
import ma.movieTales.movie_service.Repositories.GenreRepository;
import ma.movieTales.movie_service.Repositories.MovieRepository;
import ma.movieTales.movie_service.Repositories.ProductionCompanyRepository;
import ma.movieTales.movie_service.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieServiceImp implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final ProductionCompanyRepository productionCompanyRepository;
    private final MovieServiceApplication movieServiceApplication;

    @Override
    public MovieDTO getMovie(Long id) {
        Movie movie = movieRepository.findMovieById(id)
                .orElseThrow(() -> new NotFoundException("Movie with title '" + id + "' doesnt exists."));
        return MovieMapper.toDto(movie);
    }

    @Override
    @Transactional
    public MovieDTO saveMovie(MovieDTO movieDTO) {


        if (movieRepository.getMovieByTitle(movieDTO.getTitle()).isPresent()) {
            throw new AlreadyExistsException("Movie with title '" + movieDTO.getTitle() + "' already exists.");
        }

        List<Actor> movieActors = movieDTO.getActors().stream()
                .map(ActorMapper::toEntity)
                .map(actor -> actorRepository.getActorByName(actor.getName())
                        .orElseGet(() -> actorRepository.save(actor)))
                .toList();

        List<ProductionCompany> movieProdCompanies = movieDTO.getProduction_companies().stream()
                .map(ProductionCompanyMapper::toEntity)
                .map(productionCompany -> productionCompanyRepository.getProdCompanyByName(productionCompany.getName())
                        .orElseGet(() -> productionCompanyRepository.save(productionCompany)))
                .toList();

        List<Genres> movieGenres = movieDTO.getGenres().stream()
                .map(GenreMapper::toEntity)
                .map(genre -> genreRepository.getGenreByName(genre.getName())
                        .orElseGet(() -> genreRepository.save(genre)))
                .toList();

        Movie newMovie = MovieMapper.toEntity(movieDTO);

        movieActors.forEach(actor -> actor.getMovies().add(newMovie));
        movieProdCompanies.forEach(productionCompany -> productionCompany.getMovies().add(newMovie));
        movieGenres.forEach(genre -> genre.getMovies().add(newMovie));

        newMovie.setActors(movieActors);
        newMovie.setProduction_companies(movieProdCompanies);
        newMovie.setGenres(movieGenres);

        return MovieMapper.toDto(movieRepository.save(newMovie));
    }

}
