package com.lms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.lms.dto.EmailNotFoundDetails;
import com.lms.dto.NameFoundDetails;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.exception.details.NameFound;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
@ResponseBody
public class GlobalException {

	@ExceptionHandler(NameFound.class)
	public ResponseEntity<?> nameFound(NameFound nf, WebRequest wr) {

		NameFoundDetails d = new NameFoundDetails();
		d.setError("100");
		d.setMessage("User Already Registered");

		return new ResponseEntity<Object>(d, HttpStatus.FOUND);
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<?> emailNotFound(EmailNotFoundException enf, WebRequest wr) {

		EmailNotFoundDetails efd = new EmailNotFoundDetails();
		efd.setError("700");
		efd.setMessage("User with Email Not Found");

		return new ResponseEntity<Object>(efd, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

}
