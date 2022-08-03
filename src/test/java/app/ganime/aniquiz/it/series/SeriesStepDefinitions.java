package app.ganime.aniquiz.it.series;

import app.ganime.aniquiz.dto.SeriesDTO;
import app.ganime.aniquiz.it.HttpClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SeriesStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private ObjectMapper mapper;

	private String name;
	private LocalDate releaseDate;
	private JSONObject postBody;
	private final String SERIES_URI = "series";
	private final String SERIES_NAME_JSON_KEY = "name";
	private final String SERIES_RELEASE_DATE_JSON_KEY = "release_date";

	@Given("the series name is {string}")
	public void the_series_is(String name) {
		this.name = name;
	}

	@And("its release date is {int}-{int}-{int}")
	public void the_series_release_date_is(int year, int month, int day) {
		this.releaseDate = LocalDate.of(year, month, day);
	}

	@When("the user sends the series")
	public void the_user_sends_the_series() throws JSONException {
		JSONObject series = new JSONObject();
		series.put(SERIES_NAME_JSON_KEY, name);
		series.put(SERIES_RELEASE_DATE_JSON_KEY, releaseDate);

		ResponseEntity<String> response = this.httpClient.post(SERIES_URI, series.toString(), String.class);
		this.postBody = new JSONObject(response.getBody());
	}

	@Then("the series is added")
	public void the_series_is_added() throws JSONException {
		int resourceId = this.postBody.getInt("id");
		SeriesDTO series = (SeriesDTO) this.httpClient.get(SERIES_URI, resourceId, SeriesDTO.class).getBody();

		assertThat(series.getName()).isEqualTo(this.name);
		assertThat(series.getReleaseDate()).isEqualTo(this.releaseDate);
	}
}
