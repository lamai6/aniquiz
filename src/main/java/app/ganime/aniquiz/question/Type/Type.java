package app.ganime.aniquiz.question.Type;

import lombok.Getter;

@Getter
public enum Type {
	TOF("True or False ?"),
	SCQ("Single Choice Question"),
	MCQ("Multiple Choice Question"),
	FTQ("Free Text Question");

	private final String description;

	Type(String description) {
		this.description = description;
	}
}
