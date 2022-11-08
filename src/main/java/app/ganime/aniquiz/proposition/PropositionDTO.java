package app.ganime.aniquiz.proposition;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropositionDTO {
	@JsonProperty("name")
	private String name;
	@JsonProperty("correct")
	private boolean isCorrect;
}
