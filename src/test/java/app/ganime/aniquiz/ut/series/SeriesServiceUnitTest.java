package app.ganime.aniquiz.ut.series;

import app.ganime.aniquiz.config.error.exception.ResourceNotFoundException;
import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.series.SeriesRepository;
import app.ganime.aniquiz.series.SeriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SeriesServiceUnitTest {

	private List<Series> seriesList;

	@Mock
	private SeriesRepository seriesRepository;

	@InjectMocks
	private SeriesService seriesService;

	@BeforeEach
	private void setup() {
		seriesList = Stream.of(
				new Series(1L,"One Piece","Eichiro Oda", LocalDate.of(1999, 10, 20)),
				new Series(2L,"Bleach","Tite Kubo", LocalDate.of(2001, 8, 7)),
				new Series(3L,"Hajime no Ippo","Joji Morikawa", LocalDate.of(1989, 10, 11)))
			.collect(Collectors.toList());
	}

	@Test
	public void should_get_all_series() {
		given(seriesRepository.findAll()).willReturn(this.seriesList);

		List<Series> series = seriesService.getSeries();

		assertThat(series.size()).isEqualTo(3);
	}

	@Test
	public void should_get_series_by_id() {
		final Long BLEACH_ID = 2L;
		given(seriesRepository.findById(BLEACH_ID)).willReturn(seriesList.stream().filter(s -> s.getId() == 2).findFirst());

		Series series = seriesService.getSeries(BLEACH_ID);

		assertThat(series.getName()).isEqualTo("Bleach");
		assertThat(series.getAuthor()).isEqualTo("Tite Kubo");
		assertThat(series.getReleaseDate()).isEqualTo(LocalDate.of(2001, 8, 7));
	}

	@Test
	public void should_throw_exception_when_series_does_not_exist() {
		final Long NOT_EXISTING_ID = 8L;
		final String message = "The resource you requested is not found";
		given(seriesRepository.findById(NOT_EXISTING_ID)).willThrow(ResourceNotFoundException.class);

		Throwable thrown = catchThrowable(() -> seriesService.getSeries(NOT_EXISTING_ID));

		assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	public void should_post_series_sent_by_user() {
		Series series = new Series(4L,"Hunter x Hunter", "Yoshihiro Togashi", LocalDate.of(1998, 3, 16));
		given(seriesRepository.save(any(Series.class))).will(returnsFirstArg());

		Series seriesSaved = seriesService.saveSeries(series);

		assertThat(seriesSaved.getName()).isEqualTo(series.getName());
		assertThat(seriesSaved.getAuthor()).isEqualTo(series.getAuthor());
		assertThat(seriesSaved.getReleaseDate()).isEqualTo(series.getReleaseDate());
	}
}
