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

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Type type;

	@Enumerated(EnumType.STRING)
	@Column(name = "difficulty")
	private Difficulty difficulty;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "series_id", referencedColumnName = "id")
	private Series series;

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private Contributor contributor;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	@JsonManagedReference("question-title")
	private List<Title> titles;
}
