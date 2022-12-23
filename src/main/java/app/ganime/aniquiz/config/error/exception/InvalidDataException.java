package app.ganime.aniquiz.config.error.exception;

import app.ganime.aniquiz.config.error.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidDataException extends ApiException {
	public InvalidDataException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
