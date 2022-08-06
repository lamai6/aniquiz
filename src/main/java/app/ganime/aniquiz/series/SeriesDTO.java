package app.ganime.aniquiz.series;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SeriesDTO {
	private String name;
	private String author;
	private LocalDate releaseDate;
}
