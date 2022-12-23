package app.ganime.aniquiz.config.error;

import app.ganime.aniquiz.config.error.exception.InvalidDataException;
import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${api.version}")
	private String apiVersion;
	@Autowired
	private MessageSource messages;


	@ExceptionHandler({ResourceNotFoundException.class, InvalidDataException.class})
	protected ResponseEntity<Object> handleLogicExceptions(ApiException ex, WebRequest request) {
		Map<String, Object> error = ApiErrorDTO.getMapFromErrors(
			apiVersion,
			ex.getStatus().value(),
			ex.getStatus().getReasonPhrase(),
			List.of(getErrorFromException(ex)));
		return handleExceptionInternal(ex, error, new HttpHeaders(), ex.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ApiErrorDTO.Error> errors =  fieldErrors.stream()
			.map(e -> {
				return ApiErrorDTO.Error.builder()
					.domain(e.getObjectName())
					.reason(ex.getClass().getSimpleName())
					.message(e.getDefaultMessage())
					.build();
			})
			.collect(Collectors.toList());

		Map<String, Object> errorMap = ApiErrorDTO.getMapFromErrors(
			apiVersion,
			HttpStatus.BAD_REQUEST.value(),
			messages.getMessage("contributor.registration.failed", null, Locale.getDefault()),
			errors);
		return handleExceptionInternal(ex, errorMap, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
																   HttpHeaders headers,
																   HttpStatus status,
																   WebRequest request) {
		String body = "Acceptable MIME type: " + MediaType.APPLICATION_JSON_VALUE;
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	private ApiErrorDTO.Error getErrorFromException(Exception ex) {
		return ApiErrorDTO.Error.builder()
			.domain(ex.getStackTrace()[0].getFileName().split("\\.")[0])
			.reason(ex.getClass().getSimpleName())
			.message(ex.getMessage())
			.build();
	}
}
