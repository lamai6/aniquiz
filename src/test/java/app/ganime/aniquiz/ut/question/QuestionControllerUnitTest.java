package app.ganime.aniquiz.ut.question;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Question;
import app.ganime.aniquiz.question.QuestionController;
import app.ganime.aniquiz.question.QuestionDTO;
import app.ganime.aniquiz.question.QuestionService;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.title.Title;
import app.ganime.aniquiz.title.TitleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerUnitTest {

	private MockMvc mvc;
	private ObjectMapper mapper;
	private List<Question> questionList;

	@Mock
	private QuestionService service;
	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	private QuestionController controller;

	public QuestionControllerUnitTest() {
		this.mapper = new ObjectMapper();
	}

	@BeforeEach
	public void setup() {
		this.mapper = JsonMapper.builder().findAndAddModules().build();
		JacksonTester.initFields(this, mapper);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();

		Question question = new Question(1L, Type.SCQ, Difficulty.E, LocalDateTime.now(), null, null, null);
		Language en = new Language(1L, Locale.ENGLISH, null);
		Title title = new Title(question, en, "What is the devil fruit of Monkey D. Luffy ?", null);
		Proposition proposition1 = new Proposition(1L, "Gomu Gomu no Mi", title);
		Proposition proposition2 = new Proposition(2L, "Mera Mera no Mi", title);

		title.setPropositions(List.of(proposition1, proposition2));
		question.setTitles(List.of(title));
		en.setTitles(List.of(title));
		questionList = List.of(question);
	}

	@Test
	public void shouldRetrieveByIdWhenExists() throws Exception {
		Question question = questionList.stream().findFirst().orElse(null);
		QuestionDTO questionDTO = QuestionDTO.builder()
			.type(question.getType())
			.difficulty(question.getDifficulty())
			.createdAt(question.getCreatedAt())
			.titles(question.getTitles().stream().map(t -> mapper.convertValue(t, TitleDto.class)).collect(Collectors.toList()))
			.build();
		given(service.getQuestion(1L)).willReturn(question);
		given(modelMapper.map(any(Question.class), eq(QuestionDTO.class))).willReturn(questionDTO);

		MockHttpServletResponse response = mvc.perform(get("/questions/1").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONObject body = new JSONObject(response.getContentAsString());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.getString("type")).isEqualTo(Type.SCQ.getDescription());
		assertThat(body.getString("difficulty")).isEqualTo(Difficulty.E.getDescription());
		assertThat(LocalDateTime.parse(body.getString("created_at"))).isEqualTo(question.getCreatedAt());
	}

}
