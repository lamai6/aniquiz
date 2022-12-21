package app.ganime.aniquiz.it.series;

import app.ganime.aniquiz.it.HttpClient;
import app.ganime.aniquiz.series.SeriesDTO;
import app.ganime.aniquiz.series.SeriesRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class SeriesStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private SeriesRepository repository;

	private String name;
	private String author;
	private LocalDate releaseDate;
	private int newSeriesId;
	private final String SERIES_URI = "series";

	@After
	private void cleanSeries() {
		 repository.deleteById((long) newSeriesId);
	}

	@And("the series name is {string}")
	public void the_series_is(String name) {
		this.name = name;
	}

	@And("its author is {string}")
	public void the_series_author_is(String author) {
		this.author = author;
	}

	@And("its release date is {int}-{int}-{int}")
	public void the_series_release_date_is(int year, int month, int day) {
		this.releaseDate = LocalDate.of(year, month, day);
	}

	@When("the user sends the series")
	public void the_user_sends_the_series() throws JSONException {
		JSONObject series = createSeriesJson();
		this.newSeriesId = (int) this.httpClient.post(SERIES_URI, series.toString(), Integer.class).getBody();
	}

	@Then("the series is added")
	public void the_series_is_added() throws JSONException {
		SeriesDTO series = (SeriesDTO) this.httpClient.get(SERIES_URI, newSeriesId, SeriesDTO.class).getBody();

		assertThat(series.getName()).isEqualTo(this.name);
		assertThat(series.getReleaseDate()).isEqualTo(this.releaseDate);
	}

	private JSONObject createSeriesJson() throws JSONException {
		JSONObject series = new JSONObject();
		String nameKey = "name";
		String authorKey = "author";
		String releaseDateKey = "release_date";

		series.put(nameKey, name);
		series.put(authorKey, author);
		series.put(releaseDateKey, releaseDate);
		return series;
	}
}
