package com.example.CompanyService.Company.Messaging;

import com.example.CompanyService.Company.CompanyServices;
import com.example.CompanyService.Company.dto.ReviewMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewMessageConsumer {
    private final CompanyServices companyServices;


    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage){
        companyServices.updateRating(reviewMessage);
    }
}
