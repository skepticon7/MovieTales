package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.DTO.GenreDTO;
import ma.movieTales.movie_service.Entity.Actor;
import ma.movieTales.movie_service.Entity.Genres;

import java.util.ArrayList;

public class GenreMapper {
    public static GenreDTO toDto(Genres genres){
        if(genres == null) return null;
        return GenreDTO.builder()
                .name(genres.getName())
                .build();
    }

    public static Genres toEntity(GenreDTO genreDTO){
        if(genreDTO == null) return null;
        return Genres.builder()
                .name(genreDTO.getName())
                .movies(new ArrayList<>())
                .build();
    }
}
