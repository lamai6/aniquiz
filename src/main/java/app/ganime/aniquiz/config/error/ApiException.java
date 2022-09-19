package app.ganime.aniquiz.config.error;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {

	public ApiException(HttpStatus status, String message) {
		super(status, message);
	}

	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(this.getReason(), this.getCause());
	}
}

