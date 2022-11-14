package app.ganime.aniquiz.question.Type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Type {
	TOF("True or False ?"),
	SCQ("Single Choice Question"),
	MCQ("Multiple Choice Question"),
	FTQ("Free Text Question");

	@Getter(onMethod_ = @JsonValue)
	private final String description;

	Type(String description) {
		this.description = description;
	}

	@JsonCreator
	public static Type getEnumFrom(String value) {
		return Type.valueOf(value);
	}
}
