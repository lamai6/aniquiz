package app.ganime.aniquiz.config.error.exception;

import app.ganime.aniquiz.config.error.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
	private static final String MESSAGE = "The resource you requested is not found";

	public ResourceNotFoundException() {
		super(HttpStatus.NOT_FOUND, MESSAGE);
	}
}
