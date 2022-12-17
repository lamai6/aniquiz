package app.ganime.aniquiz.ut.contributor;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.contributor.Contributor;
import app.ganime.aniquiz.contributor.ContributorRepository;
import app.ganime.aniquiz.contributor.ContributorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ContributorServiceUnitTest {

	private List<Contributor> contributorList;
	@Mock
	public ContributorRepository repository;
	@Mock
	public PasswordEncoder encoder;
	@InjectMocks
	public ContributorService service;

	@BeforeEach
	private void setup() {
		contributorList = Stream.of(
				new Contributor(1L, "akagami", "shanks92@gmail.com", "@kagami92", "CONTRIBUTOR", LocalDateTime.now()),
				new Contributor(2L, "kurosaki", "ichigo95@outlook.com", "bankai", "CONTRIBUTOR", LocalDateTime.now()))
			.collect(Collectors.toList());
	}

	@Test
	public void shouldGetAllContributors() {
		given(repository.findAll()).willReturn(contributorList);

		List<Contributor> contributors = service.getContributors();

		assertThat(contributors.size()).isEqualTo(2);
	}

	@Test
	public void shouldGetContributorById() {
		final Long CONTRIBUTOR_ID = 1L;
		given(repository.findById(anyLong())).willReturn(contributorList.stream().filter(c -> c.getId() == CONTRIBUTOR_ID).findFirst());

		Contributor contributor = service.getContributor(CONTRIBUTOR_ID);

		assertThat(contributor.getUsername()).isEqualTo("akagami");
		assertThat(contributor.getEmail()).isEqualTo("shanks92@gmail.com");
	}

	@Test
	public void shouldThrowExceptionWhenContributorDoesNotExist() {
		final Long NOT_EXISTING_ID = 8L;
		final String message = "The resource you requested is not found";
		given(repository.findById(NOT_EXISTING_ID)).willThrow(ResourceNotFoundException.class);

		Throwable thrown = catchThrowable(() -> service.getContributor(NOT_EXISTING_ID));

		assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	public void shouldSaveContributorRegistration() {
		Contributor contributor = new Contributor(3L, "roronoa95", "roronoa95@gmail.com", "onepiece", "CONTRIBUTOR", LocalDateTime.now());
		given(repository.save(any(Contributor.class))).will(returnsFirstArg());

		Contributor contributorSaved = service.saveContributor(contributor);

		assertThat(contributorSaved.getUsername()).isEqualTo(contributor.getUsername());
		assertThat(contributorSaved.getEmail()).isEqualTo(contributor.getEmail());
		assertThat(contributorSaved.getCreatedAt()).isEqualTo(contributor.getCreatedAt());
	}
}
