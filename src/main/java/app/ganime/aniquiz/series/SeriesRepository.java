package app.ganime.aniquiz.series;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends PagingAndSortingRepository<Series, Long> {
	@Override
	List<Series> findAll();

	Optional<Series> findByName(String name);
}
