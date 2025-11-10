package com.performance.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformanceRating rating;
    
    @Column(nullable = false, length = 500)
    private String comments;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "review_date", nullable = false)
    private Date reviewDate;
    
    @Column(name = "reviewer_name", length = 50)
    private String reviewerName;
    
    // Constructors
    public Review() {
        this.reviewDate = new Date();
    }
    
    public Review(Employee employee, PerformanceRating rating, String comments, String reviewerName) {
        this.employee = employee;
        this.rating = rating;
        this.comments = comments;
        this.reviewerName = reviewerName;
        this.reviewDate = new Date();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public PerformanceRating getRating() {
        return rating;
    }
    
    public void setRating(PerformanceRating rating) {
        this.rating = rating;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Date getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", reviewDate=" + reviewDate +
                ", reviewerName='" + reviewerName + '\'' +
                '}';
    }
}