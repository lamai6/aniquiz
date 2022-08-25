package app.ganime.aniquiz.ut.series;

import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.series.SeriesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class SeriesRepositoryUnitTest {

	@Autowired
	private SeriesRepository repository;

	private List<Series> databaseSeries;

	@BeforeAll
	public void initData() {
		databaseSeries = Stream.of(
				new Series(1,"One Piece","Eichiro Oda", LocalDate.of(1999, 10, 20)),
				new Series(2,"Bleach","Tite Kubo", LocalDate.of(2001, 8, 7)),
				new Series(3,"Hajime no Ippo","Joji Morikawa", LocalDate.of(1989, 10, 11)))
			.collect(Collectors.toList());
	}

	@BeforeEach
	public void saveData() {
		repository.saveAll(databaseSeries);
	}

	@AfterEach
	public void clean() {
		repository.deleteAll();
	}

	@Test
	public void should_get_series_when_exists() {
		Long ONE_PIECE_ID = 1L;

		Series series = repository.findById(ONE_PIECE_ID).orElse(null);

		assertThat(series.getName()).isEqualTo("One Piece");
		assertThat(series.getAuthor()).isEqualTo("Eichiro Oda");
		assertThat(series.getReleaseDate()).isEqualTo(LocalDate.of(1999, 10, 20));
	}

	@Test
	public void should_get_all_series() {
		List<Series> seriesList = repository.findAll();

		assertThat(seriesList.size()).isEqualTo(3);
	}

	@Test
	public void should_add_series() {
		Series series = new Series(4,"Hunter x Hunter", "Yoshihiro Togashi", LocalDate.of(1998, 3, 16));

		Series seriesAdded = repository.save(series);

		assertThat(repository.findAll().size()).isEqualTo(4);
		assertThat(seriesAdded.getName()).isEqualTo("Hunter x Hunter");
		assertThat(seriesAdded.getAuthor()).isEqualTo("Yoshihiro Togashi");
		assertThat(seriesAdded.getReleaseDate()).isEqualTo(LocalDate.of(1998, 3, 16));
	}
}
