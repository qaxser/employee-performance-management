package com.performance.model;

public enum PerformanceRating {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    AVERAGE("Average"),
    BELOW_AVERAGE("Below Average"),
    POOR("Poor");
    
    private final String displayName;
    
    PerformanceRating(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static PerformanceRating fromDisplayName(String displayName) {
        for (PerformanceRating rating : values()) {
            if (rating.displayName.equals(displayName)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}