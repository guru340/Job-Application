package com.example.ReviewServices.Review;

import java.util.List;

public interface ReviewServices {
    List<Review> getAllReviews(Long CompanyId);
    public boolean addReview(Long companyId, Review review);
    Review getReview( Long reviewId);


    boolean updateReview(Long reviewId, Review updatedReview);

    boolean deleteReview(Long reviewId);
}
