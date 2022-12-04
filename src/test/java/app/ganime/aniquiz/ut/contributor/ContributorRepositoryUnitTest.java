package app.ganime.aniquiz.ut.contributor;

import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.contributor.ContributorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(classes = SecurityAutoConfiguration.class)
@TestPropertySource(properties = {
	"spring.datasource.url=jdbc:h2:mem:aniquiz_ut;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL",
	"spring.jpa.hibernate.ddl-auto=update",
	"spring.main.web-application-type=servlet"
})
public class ContributorRepositoryUnitTest {

	@Autowired
	private ContributorRepository repository;

	private static List<Contributor> contributorDB;

	@BeforeAll
	public static void initData() {
		contributorDB = Stream.of(
				new Contributor(1L, "akagami", "shanks92@gmail.com", "@kagami92", LocalDateTime.now()),
				new Contributor(2L, "kurosaki", "ichigo95@outlook.com", "bankai", LocalDateTime.now()))
			.collect(Collectors.toList());
	}

	@BeforeEach
	public void saveData() {
		repository.saveAll(contributorDB);
	}

	@AfterEach
	public void clean() {
		repository.deleteAll();
	}

	@Test
	public void shouldGetContributorWhenExists() {
		Long ID = 1L;

		Contributor contributor = repository.findById(ID).orElse(null);

		assertThat(contributor.getUsername()).isEqualTo("akagami");
		assertThat(contributor.getEmail()).isEqualTo("shanks92@gmail.com");
		assertThat(contributor.getPassword()).isEqualTo("@kagami92");
	}

	@Test
	public void shouldGetAllContributors() {
		List<Contributor> contributorList = repository.findAll();

		assertThat(contributorList.size()).isEqualTo(2);
	}

	@Test
	public void shouldAddContributor() {
		Contributor contributor = new Contributor(3L, "roronoa95", "roronoa95@gmail.com", "onepiece", LocalDateTime.now());

		Contributor contributorSaved = repository.save(contributor);

		assertThat(repository.findAll().size()).isEqualTo(3);
		assertThat(contributorSaved.getUsername()).isEqualTo(contributor.getUsername());
		assertThat(contributorSaved.getEmail()).isEqualTo(contributor.getEmail());
		assertThat(contributorSaved.getPassword()).isEqualTo(contributor.getPassword());
		assertThat(contributorSaved.getCreatedAt()).isEqualTo(contributor.getCreatedAt());
	}
}
