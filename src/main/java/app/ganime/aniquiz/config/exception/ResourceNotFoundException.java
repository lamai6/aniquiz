package app.ganime.aniquiz.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {
	private static final String MESSAGE = "The resource you requested is not found";

	public ResourceNotFoundException() {
		super(HttpStatus.NOT_FOUND, MESSAGE);
	}
}
