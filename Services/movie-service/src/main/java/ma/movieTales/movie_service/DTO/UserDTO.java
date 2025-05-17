package ma.movieTales.movie_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserDTO {
    private String _id;
    private String avatarUrl;
    private String username;
}
