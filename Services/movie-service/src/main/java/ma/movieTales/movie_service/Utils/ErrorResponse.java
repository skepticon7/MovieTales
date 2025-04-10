package ma.movieTales.movie_service.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String error;
    private int status;
    private long timestamp;
}
