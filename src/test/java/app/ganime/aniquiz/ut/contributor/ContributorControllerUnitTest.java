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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class ContributorControllerUnitTest {

	private MockMvc mvc;
	private List<Contributor> contributorList;
	private JacksonTester json;
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
				new Contributor(1L, "akagami", "shanks92@gmail.com", "@kagami92", "ROLE_CONTRIBUTOR", LocalDateTime.now()),
				new Contributor(2L, "kurosaki", "ichigo95@outlook.com", "bankai", "ROLE_CONTRIBUTOR", LocalDateTime.now()))
			.collect(Collectors.toList());
	}

	@Test
	public void shouldRetrieveByIdWhenExists() throws Exception {
		Contributor contributor = this.contributorList.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
		ContributorDTO contributorDTO = ContributorDTO.builder()
			.username(contributor.getUsername())
			.email(contributor.getEmail())
			.createdAt(contributor.getCreatedAt())
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
		assertThat(LocalDateTime.parse(body.getString("created_at"))).isEqualTo(contributor.getCreatedAt());
	}

	@Test
	public void shouldRetrieveAllContributors() throws Exception {
		given(service.getContributors()).willReturn(this.contributorList);
		given(mapper.map(any(Contributor.class), eq(ContributorDTO.class))).willAnswer((invocation) -> {
			Contributor currContributor = invocation.getArgument(0);
			ContributorDTO dto = ContributorDTO.builder()
				.username(currContributor.getUsername())
				.email(currContributor.getEmail())
				.createdAt(currContributor.getCreatedAt())
				.build();
			return dto;
		});

		MockHttpServletResponse response = mvc.perform(get("/contributors").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONArray body = new JSONArray(response.getContentAsString());
		JSONObject firstContributor = body.getJSONObject(0);
		Contributor contributor = this.contributorList.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.length()).isEqualTo(2);
		assertThat(firstContributor.getString("username")).isEqualTo(contributor.getUsername());
		assertThat(firstContributor.getString("email")).isEqualTo(contributor.getEmail());
		assertThat(firstContributor.getString("password")).isEqualTo("null");
		assertThat(LocalDateTime.parse(firstContributor.getString("created_at"))).isEqualTo(contributor.getCreatedAt());
	}

	@Test
	public void shouldAddNewContributor() throws Exception {
		ContributorDTO contributorDTO = ContributorDTO.builder()
			.username("roronoa95")
			.email("roronoa95@gmail.com")
			.password("onepiece")
			.createdAt(LocalDateTime.now())
			.build();
		Contributor contributor = new Contributor(
			3L,
			contributorDTO.getUsername(),
			contributorDTO.getEmail(),
			contributorDTO.getPassword(),
			contributorDTO.getRoles(),
			contributorDTO.getCreatedAt());
		given(mapper.map(any(ContributorDTO.class), eq(Contributor.class))).willReturn(contributor);
		given(service.saveContributor(any(Contributor.class))).will(returnsFirstArg());

		MockHttpServletResponse response = mvc.perform(post("/contributors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(contributorDTO).getJson()))
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getContentAsString()).isEqualTo("3");
	}
}
