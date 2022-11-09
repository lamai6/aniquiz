package app.ganime.aniquiz.question;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
	@Override
	List<Question> findAll();
}
