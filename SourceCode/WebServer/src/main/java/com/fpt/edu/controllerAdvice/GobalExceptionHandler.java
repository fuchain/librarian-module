package com.fpt.edu.controllerAdvice;


import com.fpt.edu.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
            details.add(ex.getLocalizedMessage());
       ErrorRespone error = new ErrorRespone("Server Internal Error", details);
       return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorRespone error = new ErrorRespone("Entity Not Found", details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }








}
