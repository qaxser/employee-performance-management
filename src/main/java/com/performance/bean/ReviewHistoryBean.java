package com.performance.bean;

import com.performance.dao.EmployeeDAO;
import com.performance.dao.ReviewDAO;
import com.performance.model.Employee;
import com.performance.model.Review;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class ReviewHistoryBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private EmployeeDAO employeeDAO;
    private ReviewDAO reviewDAO;
    
    private Employee employee;
    private List<Review> reviews;
    private Long employeeId;
    
    public ReviewHistoryBean() {
        employeeDAO = new EmployeeDAO();
        reviewDAO = new ReviewDAO();
    }
    
    @PostConstruct
    public void init() {
        // Get the employee ID from session
        Object empId = FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .get("selectedEmployeeId");
        
        if (empId != null) {
            employeeId = (Long) empId;
            loadEmployeeAndReviews();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", 
                    "No employee selected."));
        }
    }
    
    private void loadEmployeeAndReviews() {
        try {
            // Load employee details
            employee = employeeDAO.getEmployeeById(employeeId);
            
            if (employee == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", 
                        "Employee not found."));
                return;
            }
            
            // Load all reviews for this employee
            reviews = reviewDAO.getReviewsByEmployeeId(employeeId);
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", 
                    "Failed to load employee reviews: " + e.getMessage()));
        }
    }
    
    public String backToDashboard() {
        // Clear the selected employee from session
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .remove("selectedEmployeeId");
        
        return "dashboard?faces-redirect=true";
    }
    
    // Getters and setters
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}