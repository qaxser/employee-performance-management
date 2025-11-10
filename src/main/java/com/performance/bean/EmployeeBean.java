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
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class EmployeeBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Fixed departments
    private static final List<String> DEPARTMENT_LIST = Arrays.asList(
        "Engineering",
        "Human Resources",
        "Sales",
        "Marketing",
        "Finance",
        "Operations"
    );
    
    private EmployeeDAO employeeDAO;
    private ReviewDAO reviewDAO;
    
    private Employee employee;
    private List<Employee> employees;
    private List<Employee> allEmployees; // Keep all employees for filtering
    
    // Search/Filter fields
    private String searchTerm;
    private String selectedDepartment;
    private Double minSalary;
    private Double maxSalary;
    
    public EmployeeBean() {
        employeeDAO = new EmployeeDAO();
        reviewDAO = new ReviewDAO();
        employee = new Employee();
    }
    
    @PostConstruct
    public void init() {
        loadEmployees();
    }
    
    public void loadEmployees() {
        allEmployees = employeeDAO.getAllEmployees();
        employees = new ArrayList<>(allEmployees);
    }
    
    public String navigateToAddEmployee() {
        employee = new Employee();
        return "addEmployee?faces-redirect=true";
    }
    
    public String navigateToPerformanceReview() {
        return "performanceReview?faces-redirect=true";
    }
    
    public String saveEmployee() {
        try {
            employeeDAO.saveEmployee(employee);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Success!", 
                    "Employee " + employee.getName() + " has been added successfully."));
            
            return "dashboard?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", 
                    "Failed to save employee: " + e.getMessage()));
            return null;
        }
    }
    
    public void searchEmployees() {
        employees = allEmployees.stream()
            .filter(emp -> {
                // Name filter
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    if (!emp.getName().equalsIgnoreCase(searchTerm)) {
                        return false;
                    }
                }
                
                // Department filter
                if (selectedDepartment != null && !selectedDepartment.trim().isEmpty()) {
                    if (!emp.getDepartment().equals(selectedDepartment)) {
                        return false;
                    }
                }
                
                // Salary range filter
                if (minSalary != null && emp.getSalary() < minSalary) {
                    return false;
                }
                if (maxSalary != null && emp.getSalary() > maxSalary) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        if (employees.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "No Results", 
                    "No employees found matching your search criteria."));
        }
    }
    
    public void clearSearch() {
        searchTerm = null;
        selectedDepartment = null;
        minSalary = null;
        maxSalary = null;
        employees = new ArrayList<>(allEmployees);
    }
    
    public void clearNameFilter() {
        searchTerm = null;
        searchEmployees();
    }
    
    public void clearDepartmentFilter() {
        selectedDepartment = null;
        searchEmployees();
    }
    
    public void clearSalaryFilter() {
        minSalary = null;
        maxSalary = null;
        searchEmployees();
    }
    
    public String cancel() {
        return "dashboard?faces-redirect=true";
    }
    
    public Review getLatestReview(Long employeeId) {
        List<Review> reviews = reviewDAO.getReviewsByEmployeeId(employeeId);
        if (reviews != null && !reviews.isEmpty()) {
            return reviews.get(0); // Assuming reviews are ordered by date DESC
        }
        return null;
    }
    
    public String viewEmployeeReviews(Long employeeId) {
        // Store the employee ID in session for the reviews page
        FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("selectedEmployeeId", employeeId);
        return "employeeReviews?faces-redirect=true";
    }
    
    // Method to get all employee names sorted alphabetically
    public List<String> getEmployeeNames() {
        return allEmployees.stream()
            .map(Employee::getName)
            .sorted()
            .distinct()
            .collect(Collectors.toList());
    }
    
    // Return the fixed list of departments
    public List<String> getDepartments() {
        return DEPARTMENT_LIST;
    }
    
    // Getters and setters
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public String getSelectedDepartment() {
        return selectedDepartment;
    }
    
    public void setSelectedDepartment(String selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }
    
    public Double getMinSalary() {
        return minSalary;
    }
    
    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }
    
    public Double getMaxSalary() {
        return maxSalary;
    }
    
    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }
}