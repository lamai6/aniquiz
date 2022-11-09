package app.ganime.aniquiz.language;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {
	private Locale locale;
}
