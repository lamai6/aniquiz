package app.ganime.aniquiz.ut.question;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageDTO;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.proposition.PropositionDTO;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Question;
import app.ganime.aniquiz.question.QuestionController;
import app.ganime.aniquiz.question.QuestionDTO;
import app.ganime.aniquiz.question.QuestionService;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.title.Title;
import app.ganime.aniquiz.title.TitleDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerUnitTest {

	private MockMvc mvc;
	private ObjectMapper mapper;
	private JacksonTester json;
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
		Title title = new Title(null, "What is the devil fruit of Monkey D. Luffy?", question, en, null);
		Proposition proposition1 = new Proposition(1L, "Gomu Gomu no Mi", true, title);
		Proposition proposition2 = new Proposition(2L, "Mera Mera no Mi", false, title);

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
			.titles(question.getTitles().stream().map(t -> mapper.convertValue(t, TitleDTO.class)).collect(Collectors.toList()))
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
		assertThat(new JSONArray(body.getString("titles")).length()).isEqualTo(1);
	}

	@Test
	public void shouldRetrieveAllQuestions() throws Exception {
		given(service.getQuestions()).willReturn(this.questionList);
		given(modelMapper.map(any(Question.class), eq(QuestionDTO.class))).willAnswer((invocation) -> {
			Question currQuestion = invocation.getArgument(0);
			QuestionDTO dto = QuestionDTO.builder()
				.type(currQuestion.getType())
				.difficulty(currQuestion.getDifficulty())
				.createdAt(currQuestion.getCreatedAt())
				.titles(currQuestion.getTitles().stream().map(t -> mapper.convertValue(t, TitleDTO.class)).collect(Collectors.toList()))
				.build();
			return dto;
		});

		MockHttpServletResponse response = mvc.perform(get("/questions").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONArray body = new JSONArray(response.getContentAsString());
		JSONObject questionJSON = body.getJSONObject(0);
		questionJSON.put("difficulty", questionJSON.get("difficulty").toString().substring(0, 1).toUpperCase());
		questionJSON.put("type", Arrays.stream(questionJSON.get("type").toString().split(" "))
			.map(w -> w.substring(0,1))
			.collect(Collectors.joining()));
		QuestionDTO questionDTO = mapper.readValue(body.getString(0), QuestionDTO.class);
		Question question = this.questionList.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.length()).isEqualTo(1);
		assertThat(questionDTO.getType()).isEqualTo(question.getType());
		assertThat(questionDTO.getDifficulty()).isEqualTo(question.getDifficulty());
		assertThat(questionDTO.getCreatedAt()).isEqualTo(question.getCreatedAt());
		assertThat(questionDTO.getTitles().size()).isEqualTo(1);
	}

	@Test
	public void shouldAddNewQuestion() throws Exception {
		PropositionDTO prop1 = PropositionDTO.builder().name("Zoro").isCorrect(true).build();
		PropositionDTO prop2 = PropositionDTO.builder().name("Shanks").isCorrect(false).build();
		PropositionDTO prop3 = PropositionDTO.builder().name("Brook").isCorrect(true).build();
		LanguageDTO en = new LanguageDTO(Locale.ENGLISH);
		TitleDTO title = new TitleDTO("Which of these characters are part of Luffy's crew?", en, List.of(prop1, prop2,prop3));
		QuestionDTO questionDTO = QuestionDTO.builder()
			.type(Type.MCQ)
			.difficulty(Difficulty.E)
			.createdAt(LocalDateTime.now())
			.titles(List.of(title))
			.build();
		Question question = new Question(
			2L,
			questionDTO.getType(),
			questionDTO.getDifficulty(),
			questionDTO.getCreatedAt(),
			null,
			null,
			questionDTO.getTitles().stream().map(t -> mapper.convertValue(t, Title.class)).collect(Collectors.toList()));
		JSONObject questionJSON = new JSONObject(mapper.writeValueAsString(questionDTO));
		questionJSON.put("difficulty", questionJSON.get("difficulty").toString().substring(0, 1).toUpperCase());
		questionJSON.put("type", Arrays.stream(questionJSON.get("type").toString().split(" "))
			.map(w -> w.substring(0,1))
			.collect(Collectors.joining()));
		given(service.saveQuestion(any(QuestionDTO.class))).willReturn(question);

		MockHttpServletResponse response = mvc.perform(post("/questions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(questionJSON.toString()))
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getContentAsString()).isEqualTo("2");
	}

}
