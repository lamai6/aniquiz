package app.ganime.aniquiz.question.Difficulty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum Difficulty {
	E("Easy"),
	N("Normal"),
	H("Hard"),
	I("Impossible");

	@Getter(onMethod_ = @JsonValue)
	private final String description;

	Difficulty(String description) {
		this.description = description;
	}

	@JsonCreator
	public static Difficulty getEnumFrom(String value) {
		try {
			return Difficulty.valueOf(value);
		} catch (IllegalArgumentException e) {
			return Stream.of(Difficulty.class.getEnumConstants())
				.filter(Difficulty -> Difficulty.getDescription().equals(value))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
		}
	}
}
