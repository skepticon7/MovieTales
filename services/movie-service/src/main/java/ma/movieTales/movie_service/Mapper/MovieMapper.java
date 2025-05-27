package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.MovieDTO;
import ma.movieTales.movie_service.Entity.Movie;

import java.util.ArrayList;

public class MovieMapper {

    public static MovieDTO toDto(Movie movie){
        if(movie == null) return null;
        return MovieDTO.builder()
                .id(movie.getId())
                .reviews(movie.getReviews().stream().map(ReviewMapper::toDto).toList())
                .vote_average(movie.getVote_average())
                .vote_count(movie.getVote_count())
                .build();
    }

    public static Movie toEntity(Long movieId){
        return Movie.builder()
                .id(movieId)
                .reviews(new ArrayList<>())
                .vote_average(0F)
                .vote_count(0L)
                .build();
    }

}
