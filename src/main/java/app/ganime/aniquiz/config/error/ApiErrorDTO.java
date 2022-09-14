package app.ganime.aniquiz.config.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ApiErrorDTO {

	private final String apiVersion;
	private final ErrorBlock error;

	public static Map<String, Object> getMapFromDefaultAttributeMap(final String apiVersion,
													  final Map<String, Object> defaultErrorAttributes) {
		Error error = Error.builder()
			.domain((String) defaultErrorAttributes.getOrDefault("path", "no domain available"))
			.reason((String) defaultErrorAttributes.getOrDefault("error", "no reason available"))
			.message((String) defaultErrorAttributes.get("message"))
			.build();

		ErrorBlock errorBlock = ErrorBlock.builder()
			.code(((Integer) defaultErrorAttributes.get("status")))
			.message((String) defaultErrorAttributes.getOrDefault("message", "no message available"))
			.errors(List.of(error))
			.build();

		return Map.of("apiVersion", apiVersion, "error", errorBlock);
//		return ApiErrorDTO.builder()
//			.apiVersion("1.0")
//			.error(errorBlock)
//			.build();
	}

	@Getter
	@Setter
	@Builder
	private static final class ErrorBlock {
		private final int code;
		private final String message;
		private final List<Error> errors;
	}

	@Getter
	@Setter
	@Builder
	private static final class Error {
		private final String domain;
		private final String reason;
		private final String message;
	}
}
