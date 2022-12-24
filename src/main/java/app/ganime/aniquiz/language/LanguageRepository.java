package app.ganime.aniquiz.language;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface LanguageRepository extends PagingAndSortingRepository<Language, Long> {
	@Override
	@QueryHints({
		@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")
	})
	List<Language> findAll();

	@QueryHints({
		@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")
	})
	Optional<Language> findByCode(Locale code);
}
