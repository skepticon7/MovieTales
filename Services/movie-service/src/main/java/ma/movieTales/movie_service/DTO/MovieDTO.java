package ma.movieTales.movie_service.DTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.movieTales.movie_service.Entity.Actor;
import ma.movieTales.movie_service.Entity.Genres;
import ma.movieTales.movie_service.Entity.ProductionCompany;
import ma.movieTales.movie_service.Entity.Review;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class MovieDTO {
    private String title;
    private String poster_url;
    private Double popularity;
    private Float vote_average;
    private String overview;
    private LocalDate release_date;
    private List<ActorDTO> actors;
    private LocalTime runtime;
    private List<GenreDTO> genres;
    private String language;
    private Long vote_count;
    private String imdbId;
    private Double budget;
    private Double revenue;
    private List<ProductionCompanyDTO> production_companies;
    private String country;
    private String tagline;
    private List<ReviewDTO> reviews;
}
