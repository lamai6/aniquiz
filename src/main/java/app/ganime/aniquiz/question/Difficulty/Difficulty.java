package app.ganime.aniquiz.question.Difficulty;

import lombok.Getter;

@Getter
public enum Difficulty {
	E("Easy"),
	N("Normal"),
	H("Hard"),
	I("Impossible");

	private final String description;

	Difficulty(String description) {
		this.description = description;
	}
}
