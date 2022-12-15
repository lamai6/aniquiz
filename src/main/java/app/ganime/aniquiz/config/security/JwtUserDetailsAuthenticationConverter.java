package app.ganime.aniquiz.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JwtUserDetailsAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final JpaUserDetailsService userDetailsService;
	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	public JwtUserDetailsAuthenticationConverter(JpaUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
		this.jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		this.jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
		String principalClaimValue = jwt.getClaimAsString("sub");
		UserDetails user = userDetailsService.loadUserByUsername(principalClaimValue);
		return new JwtUserDetailsAuthenticationToken(jwt, user, authorities, principalClaimValue);
	}
}
