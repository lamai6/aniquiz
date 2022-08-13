package app.ganime.aniquiz.series;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SeriesController {

	@Autowired
	private SeriesService seriesService;
	@Autowired
	private ObjectMapper mapper;

	@GetMapping
	public List<SeriesDTO> getSeries() {
		List<Series> series = seriesService.getSeries();
		return series.stream()
			.map(s -> mapper.convertValue(s, SeriesDTO.class))
			.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public int postSeries(@RequestBody SeriesDTO seriesDTO) {
		Series series = mapper.convertValue(seriesDTO, Series.class);
		return seriesService.saveSeries(series).getId();
	}

	@GetMapping("/{id}")
	public SeriesDTO getSeries(@PathVariable("id") int id) {
		Series series = seriesService.getSeries(id);
		return mapper.convertValue(series, SeriesDTO.class);
	}
}
