package app.ganime.aniquiz.config.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiErrorDTO {

	private String apiVersion;
	private ErrorBlock error;

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
	}

	public static Map<String, Object> getMapFromErrors(final String apiVersion,
														   int code,
														   String message,
														   List<Error> errors) {
		ErrorBlock errorBlock = ErrorBlock.builder()
			.code(code)
			.message(message)
			.errors(errors)
			.build();

		return Map.of("apiVersion", apiVersion, "error", errorBlock);
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class ErrorBlock {
		@JsonProperty("code")
		private int code;
		@JsonProperty("message")
		private String message;
		@JsonProperty("errors")
		private List<Error> errors;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Error {
		@JsonProperty("domain")
		private String domain;
		@JsonProperty("reason")
		private String reason;
		@JsonProperty("message")
		private String message;
	}
}
