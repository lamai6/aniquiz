package app.ganime.aniquiz.it.question;

import app.ganime.aniquiz.config.error.ApiErrorDTO;
import app.ganime.aniquiz.contributor.ContributorRepository;
import app.ganime.aniquiz.it.HttpClient;
import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageRepository;
import app.ganime.aniquiz.proposition.PropositionDTO;
import app.ganime.aniquiz.question.QuestionDTO;
import app.ganime.aniquiz.question.QuestionRepository;
import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.series.SeriesRepository;
import app.ganime.aniquiz.title.TitleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private SeriesRepository seriesRepository;
	@Autowired
	private ContributorRepository contributorRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private LanguageRepository languageRepository;
	private Map<String, Object> question = new HashMap<String, Object>();
	private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
	private final String QUESTION_URI = "questions";
	private String responseBody;

	@Before
	public void save() {
		Series onePiece = new Series(null,"One Piece","Eichiro Oda", LocalDate.of(1999, 10, 20));
		Series bleach = new Series(null,"Shingeki no Kyojin","Hajime Isayama", LocalDate.of(2009, 9, 9));
		seriesRepository.save(onePiece);
		seriesRepository.save(bleach);
		Language en = new Language(null, Locale.ENGLISH, null);
		languageRepository.save(en);
	}

	@After
	public void clean() {
		questionRepository.deleteAll();
		seriesRepository.deleteAll();
		contributorRepository.deleteAll();
		languageRepository.deleteAll();
	}

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

	@And("^I want to add a question$")
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
	public void the_question_is_added_to_the_API() throws JSONException {
		QuestionDTO questionAdded = (QuestionDTO) this.httpClient.get(QUESTION_URI, Integer.parseInt(this.responseBody), QuestionDTO.class).getBody();

		TitleDTO titleAdded = questionAdded.getTitles().stream().findFirst().orElse(null);
		Map titleMap = ((List<Map>) question.get("titles")).stream().findFirst().get();

		assertThat(questionAdded.getType().name()).isEqualTo(this.question.get("type"));
		assertThat(questionAdded.getDifficulty().name()).isEqualTo(this.question.get("difficulty"));
		assertThat(questionAdded.getSeries().getName()).isEqualTo(new JSONObject((Map) this.question.get("series")).getString("name"));
		assertThat(titleAdded.getName()).isEqualTo(titleMap.get("name"));
		assertThat(titleAdded.getLanguage().getCode()).isEqualTo(Locale.forLanguageTag( (String) ((Map) titleMap.get("language")).get("code") ));
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
