package ma.movieTales.movie_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.movieTales.movie_service.Enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId" , "movieId"})})
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MovieTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String userId;

    @ManyToOne
    @JoinColumn(name = "movie_id" , referencedColumnName = "id" , nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    private Status status;

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
