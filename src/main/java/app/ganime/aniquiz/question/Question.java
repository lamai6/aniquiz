package app.ganime.aniquiz.question;

import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.title.Title;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Question {
	private Long id;
	private Type type;
	private Difficulty difficulty;
	private LocalDateTime createdAt;
	private Series series;
	private Contributor contributor;
	@JsonManagedReference
	private List<Title> titles;
}
