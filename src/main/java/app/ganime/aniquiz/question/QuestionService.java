package app.ganime.aniquiz.question;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;

	public Question getQuestion(Long id) {
		return repository.findById(id).stream().findFirst().orElseThrow(ResourceNotFoundException::new);
	}

	public List<Question> getQuestions() {
		return repository.findAll();
	}

	public Question saveQuestion(Question question) {
		return repository.save(question);
	}
}
