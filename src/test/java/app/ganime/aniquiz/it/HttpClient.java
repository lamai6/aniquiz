package app.ganime.aniquiz.it;

import io.cucumber.spring.CucumberTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
public class HttpClient<T> {

	private final String HOSTNAME = "http://localhost";
	private HttpHeaders headers;
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	public HttpClient() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		this.headers = headers;
	}

	public ResponseEntity<T[]> get(String resource, Class<T[]> entity) {
		return restTemplate.getForEntity(getBaseUrl() + resource, entity);
	}

	public ResponseEntity<T> get(String resource, int id, Class<T> entity) {
		return restTemplate.getForEntity(getBaseUrl() + resource + "/" + id, entity);
	}

	public ResponseEntity<T> post(String resource, String body, Class<T> entity) {
		return restTemplate.postForEntity(getBaseUrl() + resource, new HttpEntity<>(body, this.headers), entity);
	}

	private String getBaseUrl() {
		return HOSTNAME + ":" + port + "/";
	}
}
