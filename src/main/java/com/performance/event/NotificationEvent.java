package com.performance.event;

import javax.faces.event.SystemEvent;

public class NotificationEvent extends SystemEvent {
    
    public enum NotificationType {
        SUCCESS, INFO, WARNING, ERROR
    }
    
    private String message;
    private NotificationType type;
    private String details;
    
    public NotificationEvent(Object source, String message, NotificationType type) {
        super(source);
        this.message = message;
        this.type = type;
    }
    
    public NotificationEvent(Object source, String message, NotificationType type, String details) {
        super(source);
        this.message = message;
        this.type = type;
        this.details = details;
    }
    
    public String getMessage() {
        return message;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public String getDetails() {
        return details;
    }
}