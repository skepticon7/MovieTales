package ma.movieTales.movie_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MovieStatsDTO {
    private int watched;
    private int watchlist;
    private Long voteCount;
    private Float averageRating;
}
