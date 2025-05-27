package ma.movieTales.movie_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserTrackersAndReviewsDTO {
    private List<ReviewDTO> reviews;
    private List<MovieTrackerDTO> movieTrackers;
}
