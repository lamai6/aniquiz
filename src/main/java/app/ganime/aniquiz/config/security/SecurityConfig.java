package app.ganime.aniquiz.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final RsaKeyProperties rsaKeys;

	public SecurityConfig(RsaKeyProperties rsaKeys) {
		this.rsaKeys = rsaKeys;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeRequests(auth -> auth.anyRequest().authenticated())
			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.csrf(AbstractHttpConfigurer::disable)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
				.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
			.build();
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
			.requestMatcher(new AntPathRequestMatcher("/token"))
			.authorizeRequests(auth -> auth.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.csrf(AbstractHttpConfigurer::disable)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
				.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
			.httpBasic(Customizer.withDefaults())
			.build();
	}

	@Bean
	public InMemoryUserDetailsManager users() {
		return new InMemoryUserDetailsManager(
			User
				.withUsername("lam")
				.password("{noop}password")
				.authorities("read")
				.build()
		);
	}

	@Bean
	public JwtDecoder decoder() {
		return NimbusJwtDecoder
			.withPublicKey(rsaKeys.publicKey())
			.build();
	}

	@Bean
	public JwtEncoder encoder() {
		JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
}
