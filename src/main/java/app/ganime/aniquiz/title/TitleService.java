package app.ganime.aniquiz.title;

import app.ganime.aniquiz.language.Language;
import app.ganime.aniquiz.language.LanguageService;
import app.ganime.aniquiz.proposition.Proposition;
import app.ganime.aniquiz.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TitleService {

	@Autowired
	private TitleRepository repository;
	@Autowired
	private LanguageService languageService;

	public void save(List<TitleDTO> titleDTOs, Question question) {
		List<Title> titles = new ArrayList<>();
		titleDTOs.forEach(titleDTO -> {
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

		repository.saveAll(titles);
	}
}
