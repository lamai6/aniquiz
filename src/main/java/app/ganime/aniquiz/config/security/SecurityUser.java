package app.ganime.aniquiz.config.security;

import app.ganime.aniquiz.contributor.Contributor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUser implements UserDetails {

	private final Contributor contributor;

	public SecurityUser(Contributor contributor) {
		this.contributor = contributor;
	}

	public Long getId() {
		return contributor.getId();
	}

	public Contributor getContributor() {
		return contributor;
	}

	@Override
	public String getUsername() {
		return contributor.getEmail();
	}

	@Override
	public String getPassword() {
		return contributor.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.stream(contributor
				.getRoles()
				.split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
