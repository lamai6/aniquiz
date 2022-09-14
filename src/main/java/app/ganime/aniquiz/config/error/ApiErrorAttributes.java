package app.ganime.aniquiz.config.error;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class ApiErrorAttributes extends DefaultErrorAttributes {

	@Value("${api.version}")
	private String currentApiVersion;

	@Override
	public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
		Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);
		return ApiErrorDTO.getMapFromDefaultAttributeMap(currentApiVersion, defaultErrorAttributes);
	}
}
