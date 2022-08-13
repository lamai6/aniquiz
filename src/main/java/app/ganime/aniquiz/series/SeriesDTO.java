package app.ganime.aniquiz.series;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Data
@Builder
@Jacksonized
public class SeriesDTO {

	@JsonProperty("name")
	private String name;

	@JsonProperty("author")
	private String author;

	@JsonProperty("release_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate releaseDate;
}
