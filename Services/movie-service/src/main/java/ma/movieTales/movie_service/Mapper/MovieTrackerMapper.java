package ma.movieTales.movie_service.Mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.Entity.MovieTracker;

@Data @Builder
public class MovieTrackerMapper {
    public static MovieTrackerDTO toDto(MovieTracker movieTracker){
        if(movieTracker == null) return null;
        return MovieTrackerDTO.builder()
                .userId(movieTracker.getUserId())
                .movieDTO(MovieMapper.toDto(movieTracker.getMovie()))
                .status(movieTracker.getStatus())
                .build();
    }

}
