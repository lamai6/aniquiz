package app.ganime.aniquiz.ut.question;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageRepository;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Difficulty.Difficulty;
import app.ganime.aniquiz.question.Question;
import app.ganime.aniquiz.question.QuestionRepository;
import app.ganime.aniquiz.question.Type.Type;
import app.ganime.aniquiz.title.Title;
import app.ganime.aniquiz.title.TitleId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
	"spring.datasource.url=jdbc:h2:mem:aniquiz_ut;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL",
	"spring.jpa.hibernate.ddl-auto=update"
	})
public class QuestionRepositoryUnitTest {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private LanguageRepository languageRepository;

	@BeforeEach
	public void saveData() {
		Language english = new Language(null, Locale.ENGLISH, null);
		Question question = new Question(null, Type.SCQ, Difficulty.E, LocalDateTime.now(), null, null, null);
		Title title = new Title(new TitleId(), "What is the devil fruit of Monkey D. Luffy?", null, null, null);
		Proposition proposition1 = new Proposition(null, "Gomu Gomu no Mi", true, title);
		Proposition proposition2 = new Proposition(null, "Mera Mera no Mi", false, title);
		title.setPropositions(List.of(proposition1, proposition2));
		title.setLanguage(english);
		title.setQuestion(question);
		question.setTitles(List.of(title));
		english.setTitles(List.of(title));

		languageRepository.save(english);
		questionRepository.save(question);
	}

	@AfterEach
	public void cleanDB() {
		languageRepository.deleteAll();
		questionRepository.deleteAll();
	}

	@Test
	public void shouldGetQuestionWhenExists() {
		Question question = questionRepository.findAll().stream().findFirst().orElse(null);

		assertThat(question.getType()).isEqualTo(Type.SCQ);
		assertThat(question.getDifficulty()).isEqualTo(Difficulty.E);
		assertThat(question.getTitles().size()).isEqualTo(1);
	}

	@Test
	public void shouldGetAllQuestions() {
		List<Question> questions = questionRepository.findAll();

		assertThat(questions.size()).isEqualTo(1);
	}

	@Test
	public void shouldAddQuestion() {
		Language english = languageRepository.findAll().stream().findFirst().orElse(null);
		Question question = new Question(null, Type.MCQ, Difficulty.E, LocalDateTime.now(), null, null, null);
		Title title = new Title(new TitleId(), "Which of these characters are part of Luffy's crew?", null, null, null);
		Proposition proposition1 = new Proposition(null, "Zoro", true, title);
		Proposition proposition2 = new Proposition(null, "Shanks", false, title);
		Proposition proposition3 = new Proposition(null, "Brook", true, title);
		title.setPropositions(List.of(proposition1, proposition2, proposition3));
		title.setLanguage(english);
		title.setQuestion(question);
		question.setTitles(List.of(title));
		english.setTitles(List.of(title));

		Question questionSaved = questionRepository.save(question);

		assertThat(questionRepository.findAll().size()).isEqualTo(2);
		assertThat(questionSaved.getType()).isEqualTo(question.getType());
		assertThat(questionSaved.getDifficulty()).isEqualTo(question.getDifficulty());
		assertThat(questionSaved.getCreatedAt()).isEqualTo(question.getCreatedAt());
		assertThat(questionSaved.getTitles()).isEqualTo(question.getTitles());
	}
}
