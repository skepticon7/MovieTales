package ma.movieTales.movie_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserMovieTrackersDTO {
    private UserDTO userDTO;
    List<MovieTrackerDTO> movieTrackersDTO;
}
