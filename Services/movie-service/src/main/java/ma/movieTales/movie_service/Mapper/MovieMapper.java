package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Entity.Movie;

public class MovieMapper {

    public static MovieDTO toDto(Movie movie){
        if(movie == null) return null;
        return MovieDTO.builder()
                .title(movie.getTitle())
                .poster_url(movie.getPoster_url())
                .release_date(movie.getRelease_date())
                .overview(movie.getOverview())
                .runtime(movie.getRuntime())
                .tagline(movie.getTagline())
                .actors(movie.getActors().stream().map(ActorMapper::toDto).toList())
                .genres(movie.getGenres().stream().map(GenreMapper::toDto).toList())
                .budget(movie.getBudget())
                .country(movie.getCountry())
                .imdbId(movie.getImdbId())
                .language(movie.getLanguage())
                .popularity(movie.getPopularity())
                .production_companies(movie.getProduction_companies().stream().map(ProductionCompanyMapper::toDto).toList())
                .revenue(movie.getRevenue())
                .vote_average(movie.getVote_average())
                .vote_count(movie.getVote_count())
                .build();
    }

    public static Movie toEntity(MovieDTO movieDTO){
        if(movieDTO == null) return null;
        return Movie.builder()
                .title(movieDTO.getTitle())
                .poster_url(movieDTO.getPoster_url())
                .release_date(movieDTO.getRelease_date())
                .overview(movieDTO.getOverview())
                .runtime(movieDTO.getRuntime())
                .tagline(movieDTO.getTagline())
                .imdbId(movieDTO.getImdbId())
                .actors(movieDTO.getActors().stream().map(ActorMapper::toEntity).toList())
                .genres(movieDTO.getGenres().stream().map(GenreMapper::toEntity).toList())
                .budget(movieDTO.getBudget())
                .country(movieDTO.getCountry())
                .language(movieDTO.getLanguage())
                .popularity(movieDTO.getPopularity())
                .production_companies(movieDTO.getProduction_companies().stream().map(ProductionCompanyMapper::toEntity).toList())
                .revenue(movieDTO.getRevenue())
                .vote_average(movieDTO.getVote_average())
                .vote_count(movieDTO.getVote_count())
                .build();
    }

}
