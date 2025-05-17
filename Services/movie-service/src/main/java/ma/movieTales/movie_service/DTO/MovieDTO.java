package ma.movieTales.movie_service.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class MovieDTO {

    @NotNull(message = "movie id must not be null")
    private Long id;

    @Positive(message = "vote average must be positive")
    @Min(value = 0 , message = "vote average must be at least 0")
    @Max(value = 5 , message = "vote average must be at most 5")
    private Float vote_average;



    @PositiveOrZero(message = "vote count must be positive or zero")
    private Long vote_count;




    private List<ReviewDTO> reviews;
}
