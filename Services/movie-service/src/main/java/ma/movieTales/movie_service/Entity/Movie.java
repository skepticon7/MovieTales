package ma.movieTales.movie_service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String poster_url;
    private Double popularity;
    private Float vote_average;
    private String overview;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate release_date;

    @ManyToMany(mappedBy = "movies")
    private List<Actor> actors = new ArrayList<>();

    private LocalTime runtime;

    @ManyToMany(mappedBy = "movies")

    private List<Genres> genres = new ArrayList<>();

    private String language;
    private Long vote_count;
    private Double budget;
    private Double revenue;


    @ManyToMany(mappedBy = "movies")
    private List<ProductionCompany> production_companies = new ArrayList<>();

    private String country;
    private String tagline;
    private String imdbId;

    @OneToMany(mappedBy = "movie" , cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @OneToMany(mappedBy = "movie" , cascade = CascadeType.REMOVE)
    private List<MovieTracker> movieTrackers = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
