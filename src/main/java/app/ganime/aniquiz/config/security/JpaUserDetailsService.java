package app.ganime.aniquiz.config.security;

import app.ganime.aniquiz.contributor.ContributorRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private final ContributorRepository repository;

	public JpaUserDetailsService(ContributorRepository repository) {
		this.repository = repository;
	}

	@Override
	public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
		return repository
			.findByEmail(email)
			.map(SecurityUser::new)
			.orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
	}
}
