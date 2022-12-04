package app.ganime.aniquiz.it.contributor;

import app.ganime.aniquiz.config.error.ApiErrorDTO;
import app.ganime.aniquiz.contributor.ContributorDTO;
import app.ganime.aniquiz.contributor.ContributorRepository;
import app.ganime.aniquiz.it.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ContributorStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private ContributorRepository repository;

	private ObjectMapper mapper;
	private String username;
	private String email;
	private String password;
	private String body;
	private final String CONTRIBUTOR_URI = "contributors";
	private final String USERNAME_JSON_KEY = "username";
	private final String EMAIL_JSON_KEY = "email";
	private final String PASSWORD_JSON_KEY = "password";
	private final String ID_KEY = "id";
	private final String ERROR_KEY = "error";
	private final String ERROR_MSG = "The account registration failed";
	private final String INVALID_EMAIL = "Invalid email";

	public ContributorStepDefinitions() {
		this.mapper = new ObjectMapper();
	}

	@After
	public void cleanDatabase() {
		repository.deleteAll();
	}

	@Given("the contributor username is {string}")
	public void the_contributor_name_is(String username) {
		this.username = username;
	}

	@And("its email is {string}")
	public void the_contributor_email_is(String email) {
		this.email = email;
	}

	@And("its password is {string}")
	public void the_contributor_password_is(String password) {
		this.password = password;
	}

	@When("the contributor registers")
	public void the_contributor_registers() throws JSONException {
		JSONObject contributor = new JSONObject();
		contributor.put(USERNAME_JSON_KEY, username);
		contributor.put(EMAIL_JSON_KEY, email);
		contributor.put(PASSWORD_JSON_KEY, password);

		this.body = (String) this.httpClient.post(CONTRIBUTOR_URI, contributor.toString(), String.class).getBody();
	}

	@Then("the contributor is registered")
	public void the_contributor_is_registered() throws JSONException {
		ContributorDTO contributor = (ContributorDTO) this.httpClient.get(CONTRIBUTOR_URI, Integer.parseInt(body), ContributorDTO.class).getBody();

		assertThat(contributor.getUsername()).isEqualTo(this.username);
		assertThat(contributor.getEmail()).isEqualTo(this.email);
	}

	@Then("the registration is rejected")
	public void the_registration_is_rejected() throws Exception {
		ApiErrorDTO error = mapper.readValue(body, ApiErrorDTO.class);
		ApiErrorDTO.Error emailError = error.getError().getErrors().stream().findFirst().orElse(null);

		assertThat(error.getError().getCode()).isEqualTo(400);
		assertThat(error.getError().getMessage()).isEqualTo(ERROR_MSG);
		assertThat(emailError.getMessage()).isEqualTo(INVALID_EMAIL);
	}
}
