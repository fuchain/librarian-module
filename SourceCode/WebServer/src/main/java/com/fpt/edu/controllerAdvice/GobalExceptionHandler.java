package com.fpt.edu.controllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.exception.*;
import com.mashape.unirest.http.Unirest;
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
		details.add("Entity Not Found Exception");
		ErrorResponse error = new ErrorResponse("Entity Not Found", details);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleGenneralNotFound(NotFoundException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("NOT FOUND!!!");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityPinMisMatchException.class)
	public final ResponseEntity<Object> handleMistMatch(EntityPinMisMatchException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("Entity Pin Miss Match");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(EntityAldreayExisted.class)
	public final ResponseEntity<Object> handleEntityAldreadyExisted(EntityAldreayExisted ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("Entiry aldready existed");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EntityIdMismatchException.class)
	public final ResponseEntity<Object> handleEntityAldread(EntityIdMismatchException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("Entity Id Miss Match ");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(TypeNotSupportedException.class)
	public final ResponseEntity<Object> handleEntityAldread(TypeNotSupportedException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("Type not support");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.FORBIDDEN);
	}


	@ExceptionHandler(PinExpiredException.class)
	public final ResponseEntity<Object> handlePinexpired(PinExpiredException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		details.add("PIN have expired");
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(UnirestException.class)
	public final ResponseEntity<Object> handlePinexpired(UnirestException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("Fail to do REST HTTP call");
		details.add(ex.getMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(JsonProcessingException.class)
	public final ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("Fail to Parse Json");
		details.add(ex.getMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(NoSuchMethodException.class)
	public final ResponseEntity<Object> handleException(NoSuchMethodException ex, WebRequest request) {
		logException(ex);
		List<String> details = new ArrayList<>();
		details.add("Fail to Parse Json");
		details.add(ex.getMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity(error, HttpStatus.FORBIDDEN);
	}


	private void logException(Exception ex) {
		LOGGER.error("ERROR Message :" + ex.getMessage());
		LOGGER.error("ERROR Cause :" + ex.getCause());
		LOGGER.error("ERROR StackTrace :" + ex.getStackTrace());
	}
}
