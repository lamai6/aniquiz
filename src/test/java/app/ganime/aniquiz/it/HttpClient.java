package app.ganime.aniquiz.it;

import io.cucumber.spring.CucumberTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
public class HttpClient<T> {
	private final String HOSTNAME = "http://localhost";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	public ResponseEntity<T[]> get(String resource, Class<T[]> entity) {
		return restTemplate.getForEntity(getBaseUrl() + resource, entity);
	}

	public ResponseEntity<T> get(String resource, int id, Class<T> entity) {
		return restTemplate.getForEntity(getBaseUrl() + resource + "/" + id, entity);
	}

	public ResponseEntity<T> post(String resource, String body, Class<T> entity) {
		return restTemplate.postForEntity(getBaseUrl() + resource, body, entity);
	}

	private String getBaseUrl() {
		return HOSTNAME + ":" + port + "/";
	}
}
