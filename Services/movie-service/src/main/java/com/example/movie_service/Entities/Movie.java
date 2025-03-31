package com.example.movie_service.Entities;

import com.example.movie_service.Enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Movie {

    @Id
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Roles role;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
