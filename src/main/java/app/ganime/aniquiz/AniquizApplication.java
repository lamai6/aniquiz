package app.ganime.aniquiz;

import app.ganime.aniquiz.config.security.RsaKeyProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AniquizApplication {

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(AniquizApplication.class, args);
	}

}
