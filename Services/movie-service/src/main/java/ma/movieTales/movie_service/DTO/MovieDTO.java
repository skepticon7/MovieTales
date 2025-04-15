package ma.movieTales.movie_service.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class MovieDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "poster url is required")
    private String poster_url;

    @NotNull(message = "popularity is required")
    @Positive(message = "popularity must be positive")
    private Double popularity;

    @Positive(message = "vote average must be positive")
    @Min(value = 0 , message = "vote average must be at least 0")
    @Max(value = 5 , message = "vote average must be at most 5")
    private Float vote_average;

    @NotBlank(message = "overview is required")
    private String overview;

    @NotNull(message = "release date is required")
    private LocalDate release_date;

    @NotEmpty(message = "actors are required")
    private List<ActorDTO> actors;

    @NotNull(message = "runtime is required")
    private LocalTime runtime;

    @NotEmpty(message = "genres are required")
    private List<GenreDTO> genres;

    @NotBlank(message = "language is required")
    private String language;

    @PositiveOrZero(message = "vote count must be positive or zero")
    private Long vote_count;

    @NotBlank(message = "imdb id is required")
    private String imdbId;

    @NotNull(message = "budget is required")
    @PositiveOrZero(message = "budget must be positive or zero")
    private Double budget;

    @NotNull(message = "revenue is required")
    @PositiveOrZero(message = "revenue must be positive or zero")
    private Double revenue;

    @NotEmpty(message = "production companies are required")
    private List<ProductionCompanyDTO> production_companies;

    @NotBlank(message = "country is required")
    private String country;

    @NotBlank(message = "tagline is required")
    private String tagline;

    private List<ReviewDTO> reviews;
}
