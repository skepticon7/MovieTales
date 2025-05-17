package ma.movieTales.movie_service.Mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.DTO.UserReviewsDTO;

import java.util.List;

@Data @Builder
public class UserReviewsMapper {
    public static UserReviewsDTO toDTO(UserDTO userDTO, List<ReviewDTO> reviewsDTO) {
        return UserReviewsDTO.builder()
                .userDTO(userDTO)
                .reviewsDTO(reviewsDTO)
                .build();
    }
}
