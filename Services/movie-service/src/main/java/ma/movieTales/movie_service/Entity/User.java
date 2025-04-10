package ma.movieTales.movie_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @NoArgsConstructor @AllArgsConstructor @Data
public class User {
    private String userId;
    private String username;
    private String profilePicture;
}
