package com.performance.bean;

import com.performance.dao.EmployeeDAO;
import com.performance.dao.ReviewDAO;
import com.performance.event.ReviewSubmittedEvent;
import com.performance.model.Employee;
import com.performance.model.PerformanceRating;
import com.performance.model.Review;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class ReviewBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Review review;
    private Long selectedEmployeeId;
    private List<Employee> employees;
    private EmployeeDAO employeeDAO;
    private ReviewDAO reviewDAO;
    
    @PostConstruct
    public void init() {
        employeeDAO = new EmployeeDAO();
        reviewDAO = new ReviewDAO();
        review = new Review();
        loadEmployees();
    }
    
    public void loadEmployees() {
        employees = employeeDAO.getAllEmployees(); // FIXED: Use getAllEmployees() instead of findAll()
    }
    
    public String submitReview() {
        try {
            // Validation
            if (selectedEmployeeId == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Validation Error", 
                        "Please select an employee"));
                return null;
            }
            
            if (review.getRating() == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Validation Error", 
                        "Please select a performance rating"));
                return null;
            }
            
            if (review.getComments() == null || review.getComments().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Validation Error", 
                        "Please provide comments"));
                return null;
            }
            
            if (review.getReviewerName() == null || review.getReviewerName().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Validation Error", 
                        "Please provide reviewer name"));
                return null;
            }
            
            // Get employee
            Employee employee = employeeDAO.getEmployeeById(selectedEmployeeId); // FIXED: Use getEmployeeById() instead of findById()
            if (employee == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", 
                        "Employee not found"));
                return null;
            }
            
            // Set employee and ensure review date is set
            review.setEmployee(employee);
            if (review.getReviewDate() == null) {
                review.setReviewDate(new Date());
            }
            
            // Save review
            reviewDAO.save(review);
            
            // Add success message
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Success!", 
                    "Performance review for " + employee.getName() + " has been submitted successfully."));
            
            // Trigger custom event
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                ReviewSubmittedEvent event = new ReviewSubmittedEvent(this, review);
                context.getApplication().publishEvent(context, ReviewSubmittedEvent.class, event);
            } catch (Exception e) {
                // Don't fail if event publishing fails
                System.err.println("Failed to publish event: " + e.getMessage());
            }
            
            // Reset form
            review = new Review();
            selectedEmployeeId = null;
            
            return "dashboard?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace for debugging
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", 
                    "Failed to submit review: " + e.getMessage()));
            return null;
        }
    }
    
    public String cancel() {
        review = new Review();
        selectedEmployeeId = null;
        return "dashboard?faces-redirect=true";
    }
    
    public List<PerformanceRating> getPerformanceRatings() {
        return Arrays.asList(PerformanceRating.values());
    }
    
    // Getters and Setters
    public Review getReview() {
        return review;
    }
    
    public void setReview(Review review) {
        this.review = review;
    }
    
    public Long getSelectedEmployeeId() {
        return selectedEmployeeId;
    }
    
    public void setSelectedEmployeeId(Long selectedEmployeeId) {
        this.selectedEmployeeId = selectedEmployeeId;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}