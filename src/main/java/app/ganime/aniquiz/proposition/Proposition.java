package app.ganime.aniquiz.proposition;

import app.ganime.aniquiz.title.Title;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "proposition")
public class Proposition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "is_correct")
	private boolean isCorrect;

	@ManyToOne
	@JsonBackReference("title-proposition")
	private Title title;
}
