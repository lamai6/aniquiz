package app.ganime.aniquiz.it.contributor;

import app.ganime.aniquiz.config.error.ApiErrorDTO;
import app.ganime.aniquiz.contributor.Contributor;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ContributorStepDefinitions {

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private ContributorRepository repository;
	@Autowired
	private PasswordEncoder encoder;

	private String username;
	private String email;
	private String password;
	private String body;
	private final String EMAIL_KEY = "email";
	private final String PASSWORD_KEY = "password";
	private final String CONTRIBUTOR_URI = "contributors";
	private final ObjectMapper mapper = new ObjectMapper();

	@After
	public void clean() {
		if (isNumeric(body)) repository.deleteById(Long.parseLong(body));
	}

	@Given("^I am a contributor$")
	public void i_am_a_contributor() throws JSONException {
		String username = "contrib",
			email = "contrib@aniquiz.fr",
			password = "contrib",
			roles = "CONTRIBUTOR";
		createContributor(username, email, password, roles);
	}

	@Given("^I am a contributor with admin rights$")
	public void i_am_an_admin_contributor() throws JSONException {
		String username = "admin",
			email = "admin@aniquiz.fr",
			password = "admin",
			roles = "CONTRIBUTOR,ADMIN";
		createContributor(username, email, password, roles);
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
		JSONObject contributor = createContributorJson();
		this.body = (String) this.httpClient.post(CONTRIBUTOR_URI, contributor.toString(), String.class).getBody();

		String tokenURI = "/token";
		JSONObject login = createLoginJson();
		String token = (String) this.httpClient.post(tokenURI, login.toString(), String.class).getBody();
		this.httpClient.setBearerToken(token);
	}

	@Then("the contributor is registered")
	public void the_contributor_is_registered() throws JSONException {
		ContributorDTO contributor = (ContributorDTO) this.httpClient.get(CONTRIBUTOR_URI, Integer.parseInt(body), ContributorDTO.class).getBody();

		assertThat(contributor.getUsername()).isEqualTo(this.username);
		assertThat(contributor.getEmail()).isEqualTo(this.email);
	}

	@Then("the registration is rejected")
	public void the_registration_is_rejected() throws Exception {
		String errorMessage = "The account registration failed";
		String invalidEmailMessage = "Invalid email";
		ApiErrorDTO error = mapper.readValue(body, ApiErrorDTO.class);
		ApiErrorDTO.Error emailError = error.getError().getErrors().stream().findFirst().orElse(null);

		assertThat(error.getError().getCode()).isEqualTo(400);
		assertThat(error.getError().getMessage()).isEqualTo(errorMessage);
		assertThat(emailError.getMessage()).isEqualTo(invalidEmailMessage);
	}

	private void createContributor(String username, String email, String password, String roles) throws JSONException {
		Contributor contrib = repository.save(new Contributor(null, username, email, encoder.encode(password), roles, LocalDateTime.now()));
		String token = getToken(email, password);
		httpClient.setBearerToken(token);

	}

	private String getToken(String email, String password) throws JSONException {
		JSONObject login = new JSONObject();
		login.put(EMAIL_KEY, email);
		login.put(PASSWORD_KEY, password);
		return (String) httpClient.post("/token", login.toString(), String.class).getBody();
	}

	private JSONObject createContributorJson() throws JSONException {
		String usernameKey = "username";
		JSONObject contributor = new JSONObject();
		contributor.put(usernameKey, username);
		contributor.put(EMAIL_KEY, email);
		contributor.put(PASSWORD_KEY, password);
		return contributor;
	}

	private JSONObject createLoginJson() throws JSONException {
		JSONObject login = new JSONObject();
		login.put(EMAIL_KEY, email);
		login.put(PASSWORD_KEY, password);
		return login;
	}

	private boolean isNumeric(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
