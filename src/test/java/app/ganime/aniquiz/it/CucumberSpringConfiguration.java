package app.ganime.aniquiz.it;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

	@TestConfiguration
	public static class DataSourceConfiguration {

		@Value("${SPRING_DATASOURCE_URL}")
		private String dbUrl;
		@Value("${SPRING_DATASOURCE_USERNAME}")
		private String dbUsername;
		@Value("${SPRING_DATASOURCE_PASSWORD}")
		private String dbPassword;

		@Bean
		public DataSource setDataSource() {
			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			dataSourceBuilder.url(dbUrl);
			dataSourceBuilder.username(dbUsername);
			dataSourceBuilder.password(dbPassword);
			return dataSourceBuilder.build();
		}

		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthentication("lam", "password");
		}
	}
}
