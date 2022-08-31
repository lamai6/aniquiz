package app.ganime.aniquiz.series;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SeriesRepository extends PagingAndSortingRepository<Series, Long> {
	@Override
	List<Series> findAll();
}
