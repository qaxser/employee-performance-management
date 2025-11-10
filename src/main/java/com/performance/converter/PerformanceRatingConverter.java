package com.performance.converter;

import com.performance.model.PerformanceRating;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "performanceRatingConverter", managed = true)
public class PerformanceRatingConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            return PerformanceRating.fromDisplayName(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof PerformanceRating) {
            return ((PerformanceRating) value).getDisplayName();
        }
        
        return value.toString();
    }
}