package app.ganime.aniquiz.question;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.config.security.SecurityUser;
import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageService;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.series.SeriesService;
import app.ganime.aniquiz.title.Title;
import app.ganime.aniquiz.title.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;
	@Autowired
	private SeriesService seriesService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private TitleRepository titleRepository;

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

		List<Title> titles = new ArrayList<>();
		questionDTO.getTitles().forEach(titleDTO -> {
			Language language = languageService.getLanguage(titleDTO.getLanguage().getCode());
			Title title = new Title();
			title.getId().setQuestionId(question.getId());
			title.getId().setLanguageId(language.getId());
			title.setName(titleDTO.getName());
			title.setQuestion(question);
			title.setLanguage(language);

			List<Proposition> propositions = new ArrayList<>();
			titleDTO.getPropositions().forEach(propositionDTO -> {
				Proposition proposition = new Proposition();
				proposition.setName(propositionDTO.getName());
				proposition.setCorrect(propositionDTO.isCorrect());
				proposition.setTitle(title);
				propositions.add(proposition);
			});

			title.setPropositions(propositions);
			titles.add(title);
		});

		titleRepository.saveAll(titles);
		return question;
	}
}
