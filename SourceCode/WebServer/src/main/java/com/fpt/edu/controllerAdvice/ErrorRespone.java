package com.fpt.edu.controllerAdvice;

import java.util.List;

public class ErrorRespone {
    public ErrorRespone(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    // General error message about nature of error
    private String message;

    // Specific errors in API request processing
    private List<String> details;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
