package app.ganime.aniquiz.it;

import io.cucumber.spring.CucumberTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
public class HttpClient<T> {

	private final HttpHeaders headers = new HttpHeaders();
	public String token;
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	public HttpClient() {
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	}

	public ResponseEntity<T[]> get(String resource, Class<T[]> entity) {
		return restTemplate.exchange(getBaseUrl() + resource, HttpMethod.GET, new HttpEntity<>(this.headers), entity);
	}

	public ResponseEntity<T> get(String resource, int id, Class<T> entity) {
		return restTemplate.exchange(getBaseUrl() + resource + "/" + id, HttpMethod.GET, new HttpEntity<>(this.headers), entity);
	}

	public ResponseEntity<T> post(String resource, String body, Class<T> entity) {
		return restTemplate.postForEntity(getBaseUrl() + resource, new HttpEntity<>(body, this.headers), entity);
	}

	public void setBearerToken(String token) {
		this.headers.setBearerAuth(token);
	}

	private String getBaseUrl() {
		String hostname = "http://localhost";
		return hostname + ":" + port + "/";
	}
}
