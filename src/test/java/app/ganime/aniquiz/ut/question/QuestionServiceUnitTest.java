package app.ganime.aniquiz.ut.question;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Question;
import app.ganime.aniquiz.question.QuestionRepository;
import app.ganime.aniquiz.question.QuestionService;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.title.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceUnitTest {

	private List<Question> questionList;
	@Mock
	private QuestionRepository repository;
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
		Proposition prop1 = new Proposition(3L, "Zoro", true, null);
		Proposition prop2 = new Proposition(4L, "Shanks", false, null);
		Proposition prop3 = new Proposition(5L, "Brook", true, null);
		Language en = new Language(1L, Locale.ENGLISH, null);
		Question question = new Question(2L, Type.MCQ, Difficulty.E, LocalDateTime.now(), null, null, null);
		Title title = new Title(null, "Which of these characters are part of Luffy's crew?", question, en, List.of(prop1, prop2, prop3));
		question.setTitles(List.of(title));
		given(repository.save(any(Question.class))).will(returnsFirstArg());

		Question questionSaved = service.saveQuestion(question);

		assertThat(questionSaved.getType()).isEqualTo(question.getType());
		assertThat(questionSaved.getDifficulty()).isEqualTo(question.getDifficulty());
		assertThat(questionSaved.getCreatedAt()).isEqualTo(question.getCreatedAt());
		assertThat(questionSaved.getTitles().size()).isEqualTo(1);
	}
}
