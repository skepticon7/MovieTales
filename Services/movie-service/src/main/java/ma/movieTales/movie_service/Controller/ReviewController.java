package ma.movieTales.movie_service.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.movieTales.movie_service.DTO.ReviewDTO;
import ma.movieTales.movie_service.Service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/getAllUserReviews")
    public ResponseEntity<List<ReviewDTO>> getAllUserReviews(
            @RequestParam("userId") String userId
    ) {
        List<ReviewDTO> reviews = reviewService.getUserReviews(userId);
        return new ResponseEntity<>(reviews , HttpStatus.OK);
    }

    @GetMapping("/getMovieReviews")
    public ResponseEntity<List<ReviewDTO>> getMovieReviews(
            @RequestParam("movieId") Long movieId
    ) {
        List<ReviewDTO> reviews = reviewService.getMovieReviews(movieId);
        return new ResponseEntity<>(reviews , HttpStatus.OK);
    }



    @PostMapping("/addNewReview")
    public ResponseEntity<ReviewDTO> addNewReview(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId,
            @RequestBody @Valid ReviewDTO reviewDTO
    ){
        ReviewDTO newReviewDTO = reviewService.addReviewToMovie(userId , movieId , reviewDTO);
        return new ResponseEntity<>(newReviewDTO , HttpStatus.CREATED);
    }


    @PatchMapping("/updateReview")
    public ResponseEntity<ReviewDTO> updateReview(
            @RequestParam("movieId") Long movieId,
            @RequestParam("reviewId") Long reviewId,
            @RequestParam("userId") String userId,
            @RequestBody @Valid ReviewDTO reviewDTO
    ){
        ReviewDTO updatedReview = reviewService.updateReview(userId , movieId , reviewDTO , reviewId);
        return new ResponseEntity<>(updatedReview , HttpStatus.OK);
    }


    @DeleteMapping("/deleteReview")
    public ResponseEntity<ReviewDTO> deleteReview(
            @RequestParam("userId") String userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("reviewId") Long reviewId
    ){
        ReviewDTO deletedReviewDTO = reviewService.deleteReview(userId , movieId , reviewId);
        return new ResponseEntity<>(deletedReviewDTO , HttpStatus.OK);
    }



}
