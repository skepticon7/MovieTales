package ma.movieTales.movie_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.movieTales.movie_service.Enums.Status;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MovieTrackerDTO {
    private String userId;
    private MovieDTO movieDTO;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
