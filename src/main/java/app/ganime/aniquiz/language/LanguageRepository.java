package app.ganime.aniquiz.language;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface LanguageRepository extends PagingAndSortingRepository<Language, Long> {
	@Override
	List<Language> findAll();

	Optional<Language> findByCode(Locale code);
}
