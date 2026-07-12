package com.example.ReviewServices.Review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewServices reviewServices;

    @GetMapping
    public ResponseEntity<List<Review>> getALlReview(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewServices.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReviews(@RequestParam Long companyId,@RequestBody Review review){
        boolean isreviewed=reviewServices.addReview(companyId,review);
        if(isreviewed){
            return new ResponseEntity<>("Review Added Successfully",HttpStatus.OK);

        }
        else{
            return new ResponseEntity<>("Review Not Saved",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){
        return new ResponseEntity<>(reviewServices.getReview(reviewId),
                HttpStatus.OK);

    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review review){
        boolean isReviewUpdated =reviewServices.updateReview(reviewId, review);
        if (isReviewUpdated)
            return new ResponseEntity<>("Review updated successfully",
                    HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not updated",
                    HttpStatus.NOT_FOUND);


    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        boolean isReviewDeleted = reviewServices.deleteReview(reviewId);
        if (isReviewDeleted)
            return new ResponseEntity<>("Review deleted successfully",
                    HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not deleted",
                    HttpStatus.NOT_FOUND);
    }

}
