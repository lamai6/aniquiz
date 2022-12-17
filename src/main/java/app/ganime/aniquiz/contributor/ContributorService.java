package app.ganime.aniquiz.contributor;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContributorService {

	@Autowired
	private ContributorRepository repository;
	@Autowired
	private PasswordEncoder encoder;

	public Contributor getContributor(Long id) {
		return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	public List<Contributor> getContributors() {
		return repository.findAll();
	}

	public Contributor saveContributor(Contributor contributor) {
		contributor.setPassword(encoder.encode(contributor.getPassword()));
		contributor.setRoles("CONTRIBUTOR");
		contributor.setCreatedAt(LocalDateTime.now());
		return repository.save(contributor);
	}
}
