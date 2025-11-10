package com.performance.event;

import com.performance.model.Review;
import javax.faces.event.SystemEvent;

public class ReviewSubmittedEvent extends SystemEvent {
    
    private Review review;
    private String message;
    
    public ReviewSubmittedEvent(Object source, Review review) {
        super(source);
        this.review = review;
        this.message = "Performance review submitted for " + 
                      review.getEmployee().getName() + 
                      " with rating: " + review.getRating().getDisplayName();
    }
    
    public Review getReview() {
        return review;
    }
    
    public String getMessage() {
        return message;
    }
}