package com.example.movie_service.Entities;

import com.example.movie_service.Enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(uniqueConstraints = {})
public class Review {
    @Id
    private String id;
    private String userId;
    private String movieId;
    private short rating;
    

}
