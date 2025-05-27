package ma.movieTales.movie_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Movie {
    @Id
    private Long id;


    private Float vote_average;

    private Long vote_count;


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
