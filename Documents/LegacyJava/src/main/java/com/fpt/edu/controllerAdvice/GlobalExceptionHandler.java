package com.fpt.edu.controllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.exceptions.*;
import com.fpt.edu.services.LoggingService;
import com.mashape.unirest.http.exceptions.UnirestException;
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
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger LOGGER = LogManager.getLogger(getClass());
	private final LoggingService loggingService;

	public GlobalExceptionHandler(LoggingService loggingService) {
		this.loggingService = loggingService;
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		ErrorResponse error = new ErrorResponse("Server Internal Error", details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("Entity not found exception");
		ErrorResponse error = new ErrorResponse("Entity Not Found", details);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleGeneralNotFound(NotFoundException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("NOT FOUND!!!");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityPinMisMatchException.class)
	public final ResponseEntity<Object> handleMissMatch(EntityPinMisMatchException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("Entity PIN mismatch");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(EntityAldreayExisted.class)
	public final ResponseEntity<Object> handleEntityAlreadyExisted(EntityAldreayExisted ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("Entity already existed");
		details.add(ex.toString());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EntityIdMismatchException.class)
	public final ResponseEntity<Object> handleEntityIdMismatch(EntityIdMismatchException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("Entity ID mismatch");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(TypeNotSupportedException.class)
	public final ResponseEntity<Object> handleEntityAlready(TypeNotSupportedException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("Type not support");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
	}


	@ExceptionHandler(PinExpiredException.class)
	public final ResponseEntity<Object> handlePinExpired(PinExpiredException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add(ex.toString());
		details.add("PIN have expired");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(UnirestException.class)
	public final ResponseEntity<Object> handlePinExpired(UnirestException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("Fail to do REST HTTP call");
		details.add(ex.getMessage());
		details.add(ex.toString());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public final ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("Fail to parse JSON");
		details.add(ex.toString());
		details.add(ex.getMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchMethodException.class)
	public final ResponseEntity<Object> handleException(NoSuchMethodException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("No such method");
		details.add(ex.toString());
		details.add(ex.getMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.METHOD_NOT_ALLOWED);
	}


	private void logException(Exception ex) {
		LOGGER.error("ERROR: " + ex.toString());
		LOGGER.error("ERROR Message: " + ex.getMessage());
		loggingService.logError(ex);
	}
}
