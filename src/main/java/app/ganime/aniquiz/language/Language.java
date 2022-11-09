package app.ganime.aniquiz.language;

import app.ganime.aniquiz.title.Title;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Getter
@Setter
public class Language {
	private Long id;
	private Locale locale;
	@JsonManagedReference("language-title")
	private List<Title> titles;
}
