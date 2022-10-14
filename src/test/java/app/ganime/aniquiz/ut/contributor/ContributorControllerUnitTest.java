package app.ganime.aniquiz.ut.contributor;

import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.contributor.ContributorController;
import app.ganime.aniquiz.contributor.ContributorDTO;
import app.ganime.aniquiz.contributor.ContributorService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class ContributorControllerUnitTest {

	private MockMvc mvc;
	private List<Contributor> contributorList;
	@Mock
	private ContributorService service;
	@Mock
	private ModelMapper mapper;
	@InjectMocks
	private ContributorController controller;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, JsonMapper.builder().findAndAddModules().build());
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
		contributorList = Stream.of(
				new Contributor(1L, "akagami", "shanks92@gmail.com", "@kagami92"),
				new Contributor(2L, "kurosaki", "ichigo95@outlook.com", "bankai"))
			.collect(Collectors.toList());
	}

	@Test
	public void shouldRetrieveByIdWhenExists() throws Exception {
		Contributor contributor = this.contributorList.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
		ContributorDTO contributorDTO = ContributorDTO.builder()
			.username(contributor.getUsername())
			.email(contributor.getEmail())
			.build();
		given(service.getContributor(1L)).willReturn(contributor);
		given(mapper.map(any(Contributor.class), eq(ContributorDTO.class))).willReturn(contributorDTO);

		MockHttpServletResponse response = mvc.perform(get("/contributors/1").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONObject body = new JSONObject(response.getContentAsString());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.getString("username")).isEqualTo(contributor.getUsername());
		assertThat(body.getString("email")).isEqualTo(contributor.getEmail());
	}

	@Test
	public void shouldRetrieveAllContributors() throws Exception {
		given(service.getContributors()).willReturn(this.contributorList);

		MockHttpServletResponse response = mvc.perform(get("/contributors").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONArray body = new JSONArray(response.getContentAsString());
		JSONObject firstContributor = body.getJSONObject(0);
		Contributor contributor = this.contributorList.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.length()).isEqualTo(3);
		assertThat(firstContributor.getString("username")).isEqualTo(contributor.getUsername());
		assertThat(firstContributor.getString("email")).isEqualTo(contributor.getEmail());
	}
}
