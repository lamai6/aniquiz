package app.ganime.aniquiz.proposition;

import app.ganime.aniquiz.title.Title;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Proposition {
	@JsonIgnore
	private Long id;
	private String name;
	@JsonBackReference
	private Title title;
}
