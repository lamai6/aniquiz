package app.ganime.aniquiz.question;

import app.ganime.aniquiz.contributor.ContributorDTO;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.series.SeriesDTO;
import app.ganime.aniquiz.title.TitleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
	private Type type;
	private Difficulty difficulty;
	@JsonProperty("created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;
	private SeriesDTO series;
	private ContributorDTO contributor;
	private List<TitleDto> titles;
}
