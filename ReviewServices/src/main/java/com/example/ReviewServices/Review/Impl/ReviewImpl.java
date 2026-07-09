package com.example.ReviewServices.Review.Impl;


import com.example.ReviewServices.Review.Review;
import com.example.ReviewServices.Review.ReviewRepo;
import com.example.ReviewServices.Review.ReviewServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImpl implements ReviewServices {

    private final ReviewRepo ReviewRepo;

    @Override
    public List<Review> getAllReviews(Long CompanyId) {
        List<Review> reviews=ReviewRepo.findByCompanyId(CompanyId);
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId, Review review) {

        if (companyId!=null && review !=null){
          review.setCompanyId(companyId);
            ReviewRepo.save(review);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Review getReview( Long reviewId) {
        return ReviewRepo.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review=ReviewRepo.findById(reviewId).orElse(null);
        if (reviewId != null){
            review.setTitle(updatedReview.getTitle());
            review.setId(updatedReview.getId());
            review.setDescription(updatedReview.getDescription());
            review.setCompanyId(updatedReview.getCompanyId());
            review.setRating(updatedReview.getRating());
            ReviewRepo.save(updatedReview);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review=ReviewRepo.findById(reviewId).orElse(null);
        if (review != null){
              ReviewRepo.delete(review);
            return true;
        } else {
            return false;
        }
    }
}
