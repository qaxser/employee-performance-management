package com.performance.event;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class ReviewNotificationListener implements SystemEventListener {
    
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof ReviewSubmittedEvent) {
            ReviewSubmittedEvent reviewEvent = (ReviewSubmittedEvent) event;
            
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Review Submitted Successfully",
                    reviewEvent.getMessage()
                );
                context.addMessage(null, message);
                
                System.out.println("NOTIFICATION: " + reviewEvent.getMessage());
            }
        }
    }
    
    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }
}