package app.ganime.aniquiz.proposition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropositionDTO {
	@JsonProperty("name")
	private String name;
	@JsonProperty("correct")
	private boolean isCorrect;
}
