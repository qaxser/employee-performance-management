package com.performance.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private boolean loggedIn;
    
    // Multiple valid users for demo purposes
    public String login() {
        // Check against multiple valid credentials
        boolean isValid = false;
        
        if ("admin".equals(username) && "admin".equals(password)) {
            isValid = true;
        } else if ("test".equals(username) && "test1234".equals(password)) {
            isValid = true;
        }
        
        if (isValid) {
            loggedIn = true;
            
            // Store username in session
            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loggedInUser", username);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Welcome!", 
                    "Login successful. Welcome, " + username + "!"));
            
            return "dashboard?faces-redirect=true";
        } else {
            loggedIn = false;
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Login Failed", 
                    "Invalid username or password. Please try again."));
            
            return null;
        }
    }
    
    public String logout() {
        // Get the username before clearing session
        /*String user = (String) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .get("loggedInUser");*/
        
        // Invalidate the session
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .invalidateSession();
        
        loggedIn = false;
        username = null;
        password = null;
        
        // Add logout message
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Logged Out", 
                "You have been successfully logged out. See you soon!"));
        
        return "login?faces-redirect=true";
    }
    
    // Check if user is logged in (can be used in view for rendering)
    public boolean isLoggedIn() {
        if (!loggedIn) {
            Object user = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedInUser");
            loggedIn = (user != null);
        }
        return loggedIn;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}