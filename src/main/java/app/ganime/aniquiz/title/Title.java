package app.ganime.aniquiz.title;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "title")
public class Title {

	@EmbeddedId
	private TitleId id;

	private String name;

	@ManyToOne
	@MapsId("questionId")
	@JsonBackReference("question-title")
	private Question question;

	@ManyToOne
	@MapsId("languageId")
	@JsonBackReference("language-title")
	private Language language;

	@OneToMany(mappedBy = "title", cascade = CascadeType.ALL)
	@JsonManagedReference("title-proposition")
	private List<Proposition> propositions;
}
