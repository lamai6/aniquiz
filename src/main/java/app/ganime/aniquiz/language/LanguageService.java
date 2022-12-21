package app.ganime.aniquiz.language;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LanguageService {

	@Autowired
	private LanguageRepository repository;

	public Language getLanguage(Locale code) {
		return repository.findByCode(code).orElseThrow(ResourceNotFoundException::new);
	}

}
