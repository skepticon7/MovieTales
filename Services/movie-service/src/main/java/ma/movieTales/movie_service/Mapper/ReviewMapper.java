package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Entity.Actor;
import ma.movieTales.movie_service.Entity.Review;

public class ReviewMapper {
    public static ReviewDTO toDto(Review review){
        if(review == null) return null;
        return ReviewDTO.builder()
                .stars(review.getRating())
                .description(review.getReview())
                .user(UserMapper.toDto(review.getUser()))
                .build();
    }

    public static Review toEntity(ReviewDTO reviewDTO){
        if(reviewDTO == null) return null;
        return Review.builder()
                .review(reviewDTO.getDescription())
                .rating(reviewDTO.getStars())
                .build();
    }
}
