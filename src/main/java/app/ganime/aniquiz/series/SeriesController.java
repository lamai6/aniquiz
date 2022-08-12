package app.ganime.aniquiz.series;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

	@Autowired
	private SeriesService seriesService;
	@Autowired
	private ObjectMapper mapper;

	@GetMapping
	public List<Series> getSeries() {
		return seriesService.getSeries();
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
