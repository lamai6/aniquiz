package app.ganime.aniquiz.title;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Title {
	@JsonBackReference("question-title")
	private Question question;
	@JsonBackReference("language-title")
	private Language language;
	private String name;
	@JsonManagedReference("title-proposition")
	private List<Proposition> propositions;
}
