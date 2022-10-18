package app.ganime.aniquiz.contributor;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContributorService {

	@Autowired
	public ContributorRepository repository;

	public Contributor getContributor(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}

	public List<Contributor> getContributors() {
		return repository.findAll();
	}

	public Contributor saveContributor(Contributor contributor) {
		contributor.setCreatedAt(LocalDateTime.now());
		return repository.save(contributor);
	}
}
