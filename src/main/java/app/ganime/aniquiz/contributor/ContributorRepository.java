package app.ganime.aniquiz.contributor;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContributorRepository extends PagingAndSortingRepository<Contributor, Long> {
	@Override
	List<Contributor> findAll();
}
