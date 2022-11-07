package app.ganime.aniquiz.title;

import app.ganime.aniquiz.proposition.PropositionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TitleDto {
	@JsonProperty("name")
	private String name;
	@JsonProperty("propositions")
	private List<PropositionDTO> propositions;
}
