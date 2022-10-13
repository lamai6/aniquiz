package app.ganime.aniquiz.config.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	}

	public static Map<String, Object> getMapFromExceptions(final String apiVersion,
														   int code,
														   String message,
														   List<Exception> exceptions) {
		List<Error> errors = exceptions.stream()
			.map(e -> {
				return Error.builder()
					.domain(e.getStackTrace()[0].getFileName().split("\\.")[0])
					.reason(e.getClass().getSimpleName())
					.message(e.getMessage())
					.build();
			})
			.collect(Collectors.toList());

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
	public static final class ErrorBlock {
		private final int code;
		private final String message;
		private final List<Error> errors;
	}

	@Getter
	@Setter
	@Builder
	public static final class Error {
		private final String domain;
		private final String reason;
		private final String message;
	}
}
