package com.performance.event;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class NotificationListener implements SystemEventListener {
    
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof NotificationEvent) {
            NotificationEvent notifEvent = (NotificationEvent) event;
            
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                FacesMessage.Severity severity = getSeverity(notifEvent.getType());
                
                FacesMessage message = new FacesMessage(
                    severity,
                    notifEvent.getMessage(),
                    notifEvent.getDetails()
                );
                context.addMessage(null, message);
                
                // Log to console
                System.out.println("[" + notifEvent.getType() + "] " + notifEvent.getMessage());
                if (notifEvent.getDetails() != null) {
                    System.out.println("Details: " + notifEvent.getDetails());
                }
            }
        }
    }
    
    private FacesMessage.Severity getSeverity(NotificationEvent.NotificationType type) {
        switch (type) {
            case SUCCESS:
            case INFO:
                return FacesMessage.SEVERITY_INFO;
            case WARNING:
                return FacesMessage.SEVERITY_WARN;
            case ERROR:
                return FacesMessage.SEVERITY_ERROR;
            default:
                return FacesMessage.SEVERITY_INFO;
        }
    }
    
    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }
}