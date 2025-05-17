package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.MovieTrackerDTO;
import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.DTO.UserMovieTrackersDTO;

import java.util.List;

public class UserMovieTrackersMapper {
    public static UserMovieTrackersDTO toDTO(UserDTO userDTO, List<MovieTrackerDTO> movieTrackerDTO) {
        return UserMovieTrackersDTO.builder()
                .userDTO(userDTO)
                .movieTrackersDTO(movieTrackerDTO)
                .build();
    }
}
