package app.ganime.aniquiz.proposition;

import app.ganime.aniquiz.config.error.exception.InvalidDataException;
import app.ganime.aniquiz.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class PropositionService {

	@Autowired
	private MessageSource messageSource;

	public void validatePropositions(List<Title> titles) {
		titles.forEach(title -> {
			long numberOfCorrectAnswers = getNumberOfCorrectAnswers(title.getPropositions());
			checkExistingCorrectAnswer(numberOfCorrectAnswers);

			switch (title.getQuestion().getType()) {
				case MCQ -> validateMCQ(numberOfCorrectAnswers);
				case SCQ -> validateSCQ(numberOfCorrectAnswers);
			}
		});
	}

	private void checkExistingCorrectAnswer(long numberOfCorrectAnswers) {
		if (numberOfCorrectAnswers == 0) throwException("question.invalidData");
	}

	private void validateMCQ(long numberOfCorrectAnswers) {
		if (numberOfCorrectAnswers < 2) throwException("question.invalidData.mcq");
	}

	private void validateSCQ(long numberOfCorrectAnswers) {
		if (numberOfCorrectAnswers > 1) throwException("question.invalidData.scq");
	}

	private long getNumberOfCorrectAnswers(List<Proposition> propositions) {
		return propositions.stream().filter(Proposition::isCorrect).count();
	}

	private void throwException(String messageSourceCode) {
		throw new InvalidDataException(messageSource.getMessage(
			messageSourceCode,
			null,
			Locale.getDefault()));
	}
}
