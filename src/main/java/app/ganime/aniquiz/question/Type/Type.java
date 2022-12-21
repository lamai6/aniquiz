package app.ganime.aniquiz.question.Type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

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
		try {
			return Type.valueOf(value);
		} catch (IllegalArgumentException e) {
			return Stream.of(Type.class.getEnumConstants())
				.filter(Type -> Type.getDescription().equals(value))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
		}
	}
}
