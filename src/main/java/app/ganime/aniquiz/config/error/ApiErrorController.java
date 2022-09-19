package app.ganime.aniquiz.config.error;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping({ApiErrorController.ERROR_PATH})
public class ApiErrorController extends AbstractErrorController {

	static final String ERROR_PATH = "/error";

	public ApiErrorController(final ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getError(final HttpServletRequest request) {
		Map<String, Object> body = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
		HttpStatus status = this.getStatus(request);
		return new ResponseEntity<>(body, status);
	}
}
