package com.example.ReviewServices.Review.Messaging;

import com.example.ReviewServices.Review.Review;
import com.example.ReviewServices.Review.dto.ReviewMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Review review){
        ReviewMessage reviewMessage=new ReviewMessage();
        reviewMessage.setId(review.getId());
        reviewMessage.setCompanyId(review.getCompanyId());
        reviewMessage.setDescription(review.getDescription());
        reviewMessage.setTitle(review.getTitle());
        reviewMessage.setRating(review.getRating());
        try {
            rabbitTemplate.convertAndSend("companyRatingQueue",reviewMessage);
        } catch (AmqpException exception) {
            System.out.println("RabbitMQ unavailable; review saved without rating event.");
        }
    }
}
