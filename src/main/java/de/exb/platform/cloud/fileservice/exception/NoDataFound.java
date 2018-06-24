package de.exb.platform.cloud.fileservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataFound extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NoDataFound(String message) {
		super(message);
	}
	
	public NoDataFound(String message, Exception ex) {
		super(message, ex);
	}
}
