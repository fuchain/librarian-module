package com.fpt.edu.controllerAdvice;

import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    protected final Logger LOGGER = LogManager.getLogger(getClass());


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        logException(ex);
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Internal Error", details);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logException(ex);
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorResponse error = new ErrorResponse("Entity Not Found", details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleGenneralNotFound(NotFoundException ex, WebRequest request) {
        logException(ex);
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }


    private void logException(Exception ex) {
        LOGGER.error("ERROR Message :" + ex.getMessage());
        LOGGER.error("ERROR Cause :" + ex.getCause());
        LOGGER.error("ERROR StackTrace :" + ex.getStackTrace());

    }


}