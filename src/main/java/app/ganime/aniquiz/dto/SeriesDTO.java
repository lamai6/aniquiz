package app.ganime.aniquiz.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SeriesDTO {
	private String name;
	private String author;
	private LocalDate releaseDate;
}
