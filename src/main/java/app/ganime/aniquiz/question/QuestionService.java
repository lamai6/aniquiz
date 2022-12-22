package app.ganime.aniquiz.question;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.config.security.SecurityUser;
import app.ganime.aniquiz.series.SeriesService;
import app.ganime.aniquiz.title.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;
	@Autowired
	private SeriesService seriesService;
	@Autowired
	private TitleService titleService;

	public Question getQuestion(Long id) {
		return repository.findById(id).stream().findFirst().orElseThrow(ResourceNotFoundException::new);
	}

	public List<Question> getQuestions() {
		return repository.findAll();
	}

	public Question saveQuestion(QuestionDTO questionDTO) {
		Question question = new Question();
		SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		question.setContributor(user.getContributor());
		String seriesName = questionDTO.getSeries().getName();
		question.setSeries(seriesService.getSeries(seriesName));
		question.setType(questionDTO.getType());
		question.setDifficulty(questionDTO.getDifficulty());
		question.setCreatedAt(LocalDateTime.now());
		repository.save(question);

		titleService.save(questionDTO.getTitles(), question);

		return question;
	}
}
