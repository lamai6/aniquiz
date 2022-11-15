package app.ganime.aniquiz.it.question;

import app.ganime.aniquiz.config.error.ApiErrorDTO;
import app.ganime.aniquiz.it.HttpClient;
import app.ganime.aniquiz.proposition.PropositionDTO;
import app.ganime.aniquiz.question.QuestionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	private Map<String, Object> question = new HashMap<String, Object>();
	private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
	private final String QUESTION_URI = "questions";
	private String responseBody;

	@DataTableType
	public Map<String, Object> questionEntry(Map<String, String> entry) {
		Map<String, Object> question = new HashMap<>();
		question.put("type", entry.get("type"));
		question.put("difficulty", entry.get("difficulty"));
		question.put("series", Map.of("name", entry.get("series")));
		question.put("titles", List.of(
			Map.of(
				"name", entry.get("title"),
				"language", Map.of("code", entry.get("language")))));
		return question;
	}

	@DataTableType
	public PropositionDTO propositionEntry(Map<String, String> entry) {
		return PropositionDTO.builder()
			.name(entry.get("name"))
			.isCorrect(Boolean.parseBoolean(entry.get("correct")))
			.build();
	}

	@Given("^I want to add a question, as a contributor$")
	public void i_want_to_add_a_question_as_a_contributor(List<Map<String, Object>> entry) throws JsonProcessingException {
		this.question = entry.stream().findFirst().orElse(null);
	}

	@And("^the propositions of this question are$")
	public void the_propositions_of_this_question_are(List<PropositionDTO> propositionDTOS)  throws JsonProcessingException {
		List<Map<String, Object>> currentTitles = (List<Map<String, Object>>) this.question.get("titles");
		Map<String, Object> currentTitle = currentTitles.stream().findFirst().orElse(null);
		Map<String, Object> title = Map.of(
			"name", currentTitle.get("name"),
			"language", currentTitle.get("language"),
			"propositions", propositionDTOS);
		question.put("titles", List.of(title));
	}

	@When("^I submit the question$")
	public void i_submit_the_question() throws JsonProcessingException {
		String requestBody = mapper.writeValueAsString(this.question);
		this.responseBody = (String) this.httpClient.post(QUESTION_URI, requestBody, String.class).getBody();
	}

	@Then("^the question is added to the API$")
	public void the_question_is_added_to_the_API() {
		QuestionDTO questionAdded = (QuestionDTO) this.httpClient.get(QUESTION_URI, Integer.parseInt(this.responseBody), QuestionDTO.class).getBody();

		assertThat(questionAdded.getType()).isEqualTo(this.question.get("type"));
		assertThat(questionAdded.getDifficulty()).isEqualTo(this.question.get("difficulty"));
		assertThat(questionAdded.getSeries().getName()).isEqualTo(this.question.get("series"));
		assertThat(questionAdded.getTitles().stream().findFirst().orElse(null).getName()).isEqualTo(this.question.get("title"));
		assertThat(questionAdded.getTitles().stream().findFirst().orElse(null).getLanguage().getCode())
			.isEqualTo(Locale.forLanguageTag((String) this.question.get("language")));
	}

	@Then("^the question is rejected$")
	public void the_question_is_rejected() throws Exception {
		final String ERROR_MSG = "The question has not been added due to an error";
		final String INVALID_LOGIC_MCQ = "A multiple-choice question must have at least 2 correct propositions";
		ApiErrorDTO errorDTO = mapper.readValue(this.responseBody, ApiErrorDTO.class);
		ApiErrorDTO.Error error = errorDTO.getError().getErrors().stream().findFirst().orElse(null);

		assertThat(errorDTO.getError().getCode()).isEqualTo(400);
		assertThat(errorDTO.getError().getMessage()).isEqualTo(ERROR_MSG);
		assertThat(error.getMessage()).isEqualTo(INVALID_LOGIC_MCQ);
	}
}
