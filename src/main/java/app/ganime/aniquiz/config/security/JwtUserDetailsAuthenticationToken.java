package app.ganime.aniquiz.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class JwtUserDetailsAuthenticationToken extends JwtAuthenticationToken {
	private final UserDetails user;

	public JwtUserDetailsAuthenticationToken(Jwt jwt, UserDetails user, Collection<? extends GrantedAuthority> authorities, String name) {
		super(jwt, authorities, name);
		this.user = user;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}
}
