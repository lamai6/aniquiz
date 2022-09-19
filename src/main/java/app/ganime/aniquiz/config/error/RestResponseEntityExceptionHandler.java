package app.ganime.aniquiz.config.error;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${api.version}")
	private String apiVersion;

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		Map<String, Object> error = ApiErrorDTO.getMapFromExceptions(
			apiVersion,
			ex.getStatus().value(),
			ex.getStatus().getReasonPhrase(),
			List.of(ex));
		return handleExceptionInternal(ex, error, new HttpHeaders(), ex.getStatus(), request);
	}

	@Override
	public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
																   HttpHeaders headers,
																   HttpStatus status,
																   WebRequest request) {
		String body = "Acceptable MIME type: " + MediaType.APPLICATION_JSON_VALUE;
		return handleExceptionInternal(ex, body, headers, status, request);
	}
}
