package app.ganime.aniquiz.series;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Series {
	private int id;
	private String name;
	private String author;
	private LocalDate releaseDate;
}
