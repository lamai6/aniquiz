package app.ganime.aniquiz.question.Difficulty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

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
		return Difficulty.valueOf(value);
	}
}
