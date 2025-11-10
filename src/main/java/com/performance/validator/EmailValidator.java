package com.performance.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
@FacesValidator(value = "emailValidator", managed = true)
public class EmailValidator implements Validator {
    
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        
        if (value == null) {
            return;
        }
        
        String email = value.toString();
        
        if (email.isEmpty()) {
            FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Email Required",
                "Email address is required"
            );
            throw new ValidatorException(msg);
        }
        
        if (!pattern.matcher(email).matches()) {
            FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Invalid Email Format",
                "Please enter a valid email address (e.g., user@example.com)"
            );
            throw new ValidatorException(msg);
        }
    }
}