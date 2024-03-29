package app.ganime.aniquiz.ut.series;

import app.ganime.aniquiz.series.Series;
import app.ganime.aniquiz.series.SeriesController;
import app.ganime.aniquiz.series.SeriesDTO;
import app.ganime.aniquiz.series.SeriesService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class SeriesControllerUnitTest {

	private MockMvc mvc;
	private JacksonTester<SeriesDTO> json;
	private List<Series> seriesList;

	@Mock
	private ModelMapper mapper;
	@Mock
	private SeriesService seriesService;
	@InjectMocks
	private SeriesController seriesController;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, JsonMapper.builder().findAndAddModules().build());
		mvc = MockMvcBuilders.standaloneSetup(seriesController).build();
		seriesList = Stream.of(new Series(1L,"One Piece","Eichiro Oda",LocalDate.of(1999, 10, 20)),
				new Series(2L,"Bleach","Tite Kubo",LocalDate.of(2001, 8, 7)),
				new Series(3L,"Hajime no Ippo","Joji Morikawa",LocalDate.of(1989, 10, 11)))
			.collect(Collectors.toList());
	}

	@Test
	public void shouldRetrieveByIdWhenExists() throws Exception {
		Series series = this.seriesList.stream().filter(s -> s.getId() == 2).findFirst().orElse(null);
		SeriesDTO seriesDTO = SeriesDTO.builder()
			.name(series.getName())
			.author(series.getAuthor())
			.releaseDate(series.getReleaseDate())
			.build();
		given(seriesService.getSeries(2L)).willReturn(series);
		given(mapper.map(any(Series.class), eq(SeriesDTO.class))).willReturn(seriesDTO);

		MockHttpServletResponse response = mvc.perform(get("/series/2").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONObject body = new JSONObject(response.getContentAsString());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.get("name")).isEqualTo(series.getName());
		assertThat(body.get("author")).isEqualTo(series.getAuthor());
		assertThat(LocalDate.parse(body.getString("release_date"))).isEqualTo(series.getReleaseDate());
	}

	@Test
	public void shouldRetrieveAllSeries() throws Exception {
		given(seriesService.getSeries()).willReturn(this.seriesList);

		MockHttpServletResponse response = mvc.perform(get("/series").accept(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse();

		JSONArray body = new JSONArray(response.getContentAsString());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(body.length()).isEqualTo(3);
	}

	@Test
	public void shouldAddNewSeriesSentByUser() throws Exception {
		SeriesDTO userSeries = SeriesDTO.builder()
			.name("Hunter x Hunter")
			.author("Yoshihiro Togashi")
			.releaseDate(LocalDate.of(1998, 3, 16))
			.build();
		Series series = new Series(4L, userSeries.getName(), userSeries.getAuthor(), userSeries.getReleaseDate());
		given(mapper.map(any(SeriesDTO.class), eq(Series.class))).willReturn(series);
		given(seriesService.saveSeries(any(Series.class))).will(returnsFirstArg());

		MockHttpServletResponse response = mvc.perform(post("/series")
				.contentType(MediaType.APPLICATION_JSON).content(json.write(userSeries).getJson())
			)
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getContentAsString()).isEqualTo("4");
	}
}
