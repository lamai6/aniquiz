package app.ganime.aniquiz.it.contributor;

import app.ganime.aniquiz.config.error.ApiErrorDTO;
import app.ganime.aniquiz.contributor.ContributorDTO;
import app.ganime.aniquiz.it.HttpClient;
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

	private String username;
	private String email;
	private String password;
	private JSONObject responseBody;
	private final String CONTRIBUTOR_URI = "contributor";
	private final String USERNAME_JSON_KEY = "username";
	private final String EMAIL_JSON_KEY = "email";
	private final String PASSWORD_JSON_KEY = "password";
	private final String ID_KEY = "id";
	private final String ERROR_KEY = "error";
	private final String ERROR_MSG = "The account creation failed";
	private final String INVALID_EMAIL = "Invalid email";

	@Given("the contributor name is {string}")
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

		String body = (String) this.httpClient.post(CONTRIBUTOR_URI, contributor.toString(), String.class).getBody();
		this.responseBody = new JSONObject(body);
	}

	@Then("the contributor is registered")
	public void the_contributor_is_registered() throws JSONException {
		ContributorDTO contributor = (ContributorDTO) this.httpClient.get(CONTRIBUTOR_URI, responseBody.getInt(ID_KEY), ContributorDTO.class).getBody();

		assertThat(contributor.getUsername()).isEqualTo(this.username);
		assertThat(contributor.getEmail()).isEqualTo(this.username);
	}

	@Then("the registration is rejected")
	public void the_registration_is_rejected() throws JSONException {
		ApiErrorDTO.ErrorBlock error = (ApiErrorDTO.ErrorBlock) responseBody.get(ERROR_KEY);
		ApiErrorDTO.Error emailError = error.getErrors().stream().findFirst().orElse(null);

		assertThat(error.getCode()).isEqualTo(400);
		assertThat(error.getMessage()).isEqualTo(ERROR_MSG);
		assertThat(emailError.getMessage()).isEqualTo(INVALID_EMAIL);
	}
}
