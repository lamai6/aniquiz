package app.ganime.aniquiz.question;

import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.title.Title;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "type")
	private Type type;

	@Column(name = "difficulty")
	private Difficulty difficulty;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne
	private Series series;

	@ManyToOne
	private Contributor contributor;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	@JsonManagedReference("question-title")
	private List<Title> titles;
}
