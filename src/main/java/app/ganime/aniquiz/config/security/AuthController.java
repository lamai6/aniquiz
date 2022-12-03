package app.ganime.aniquiz.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

	@Autowired
	private AuthService service;

	@PostMapping("/token")
	public String token(Authentication authentication) {
		return service.generateToken(authentication);
	}
}
