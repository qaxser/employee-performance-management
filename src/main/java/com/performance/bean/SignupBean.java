package com.performance.bean;

import com.performance.dao.UserDAO;
import com.performance.event.NotificationEvent;
import com.performance.model.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class SignupBean implements Serializable {
    
    private User user;
    private String confirmPassword;
    private UserDAO userDAO;
    
    public SignupBean() {
        user = new User();
        userDAO = new UserDAO();
    }
    
    public String signup() {
        try {
            // Check if passwords match
            if (!user.getPassword().equals(confirmPassword)) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Password Mismatch", 
                        "Passwords do not match"));
                return null;
            }
            
            // Check if username already exists
            User existingUser = userDAO.findByUsername(user.getUsername());
            if (existingUser != null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Username Taken", 
                        "This username is already registered"));
                return null;
            }
            
            // Save user
            userDAO.save(user);
            
            // Trigger signup success notification
            FacesContext context = FacesContext.getCurrentInstance();
            NotificationEvent event = new NotificationEvent(
                this,
                "Signup Successful! 🎊",
                NotificationEvent.NotificationType.SUCCESS,
                "Welcome " + user.getFullName() + "! Please login with your credentials."
            );
            context.getApplication().publishEvent(context, NotificationEvent.class, event);
            
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Signup Failed", 
                    "An error occurred: " + e.getMessage()));
            return null;
        }
    }
    
    public String cancel() {
        return "login?faces-redirect=true";
    }
    
    // Getters and Setters
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}