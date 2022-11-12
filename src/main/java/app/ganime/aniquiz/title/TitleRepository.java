package app.ganime.aniquiz.title;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TitleRepository extends PagingAndSortingRepository<Title, TitleId> {
	@Override
	List<Title> findAll();
}
