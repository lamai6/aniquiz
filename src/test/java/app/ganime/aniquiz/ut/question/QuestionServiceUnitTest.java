package app.ganime.aniquiz.ut.question;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.config.security.SecurityUser;
import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageDTO;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.proposition.PropositionDTO;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Question;
import app.ganime.aniquiz.question.QuestionDTO;
import app.ganime.aniquiz.question.QuestionRepository;
import app.ganime.aniquiz.question.QuestionService;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.series.SeriesDTO;
import app.ganime.aniquiz.series.SeriesService;
import app.ganime.aniquiz.title.Title;
import app.ganime.aniquiz.title.TitleDTO;
import app.ganime.aniquiz.title.TitleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceUnitTest {

	private List<Question> questionList;
	@Mock
	private QuestionRepository repository;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityUser user;
	@Mock
	private Contributor contributor;
	@Mock
	private SeriesService seriesService;
	@Mock
	private TitleService titleService;
	@InjectMocks
	private QuestionService service;

	@BeforeEach
	private void setup() {
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
	public void shouldGetAllQuestions() {
		given(repository.findAll()).willReturn(questionList);

		List<Question> questions = service.getQuestions();

		assertThat(questions.size()).isEqualTo(1);
	}

	@Test
	public void shouldGetQuestionById() {
		final Long QUESTION_ID = 1L;
		given(repository.findById(anyLong())).willReturn(questionList.stream().filter(c -> Objects.equals(c.getId(), QUESTION_ID)).findFirst());

		Question question = service.getQuestion(QUESTION_ID);

		assertThat(question.getType()).isEqualTo(Type.SCQ);
		assertThat(question.getDifficulty()).isEqualTo(Difficulty.E);
		assertThat(question.getTitles().size()).isEqualTo(1);
	}

	@Test
	public void shouldThrowExceptionWhenQuestionDoesNotExist() {
		final Long NOT_EXISTING_ID = 8L;
		final String message = "The resource you requested is not found";
		given(repository.findById(NOT_EXISTING_ID)).willThrow(ResourceNotFoundException.class);

		Throwable thrown = catchThrowable(() -> service.getQuestion(NOT_EXISTING_ID));

		assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	public void shouldSaveQuestionRegistration() {
		QuestionDTO questionDTO = getQuestionDTO();
		SecurityContextHolder.setContext(securityContext);
		given(securityContext.getAuthentication()).willReturn(authentication);
		given(authentication.getPrincipal()).willReturn(user);
		given(user.getContributor()).willReturn(contributor);
		Series series = new Series(1L,"One Piece","Eichiro Oda", LocalDate.of(1999, 10, 20));
		given(seriesService.getSeries(any(String.class))).willReturn(series);
		given(repository.save(any(Question.class))).will(returnsFirstArg());
		doAnswer(invocation -> {
			Question question = invocation.getArgument(1);
			question.setTitles(List.of(new Title()));
			return null;
		}).when(titleService).save(anyList(), any(Question.class));

		Question questionSaved = service.saveQuestion(questionDTO);

		assertThat(questionSaved.getType()).isEqualTo(questionDTO.getType());
		assertThat(questionSaved.getDifficulty()).isEqualTo(questionDTO.getDifficulty());
		assertThat(questionSaved.getCreatedAt()).isNotNull();
		assertThat(questionSaved.getContributor()).isNotNull();
		assertThat(questionSaved.getSeries().getName()).isEqualTo(questionDTO.getSeries().getName());
		assertThat(questionSaved.getTitles().size()).isEqualTo(1);
	}

	private QuestionDTO getQuestionDTO() {
		PropositionDTO prop1 = new PropositionDTO("Zoro", true);
		PropositionDTO prop2 = new PropositionDTO("Shanks", false);
		PropositionDTO prop3 = new PropositionDTO("Brook", true);
		LanguageDTO en = new LanguageDTO(Locale.ENGLISH);
		QuestionDTO question = QuestionDTO.builder()
			.type(Type.MCQ)
			.difficulty(Difficulty.E)
			.createdAt(LocalDateTime.now())
			.build();
		TitleDTO title = TitleDTO.builder()
			.name("Which of these characters are part of Luffy's crew?")
			.language(en)
			.propositions(List.of(prop1, prop2, prop3))
			.build();
		question.setSeries(new SeriesDTO(null, "One Piece","Eichiro Oda", LocalDate.of(1999, 10, 20)));
		question.setTitles(List.of(title));
		return question;
	}
}
