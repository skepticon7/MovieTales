package ma.movieTales.movie_service.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder @NoArgsConstructor @AllArgsConstructor @Data
public class ReviewDTO {

    @NotNull(message = "Stars must not be null")
    @Min(value = 0 , message = "Stars must be at least 0")
    @Max(value = 5 , message = "Stars must be at most 10")
    private Short rating;

    private Long id;

    private UserDTO userDTO;

    @NotEmpty(message = "Description must not be empty")
    private String review;
}
