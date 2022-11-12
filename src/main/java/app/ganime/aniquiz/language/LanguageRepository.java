package app.ganime.aniquiz.language;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LanguageRepository extends PagingAndSortingRepository<Language, Long> {
	@Override
	List<Language> findAll();
}
