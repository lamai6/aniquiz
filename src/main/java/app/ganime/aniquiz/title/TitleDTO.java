package app.ganime.aniquiz.title;

import app.ganime.aniquiz.language.LanguageDTO;
import app.ganime.aniquiz.proposition.PropositionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {
	@JsonProperty("name")
	private String name;
	@JsonProperty("language")
	private LanguageDTO language;
	@JsonProperty("propositions")
	private List<PropositionDTO> propositions;
}
