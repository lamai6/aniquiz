package app.ganime.aniquiz.contributor;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContributorRepository extends PagingAndSortingRepository<Contributor, Long> {
	@Override
	@QueryHints({
		@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")
	})
	List<Contributor> findAll();

	@QueryHints({
		@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")
	})
	Optional<Contributor> findByEmail(String email);
}
