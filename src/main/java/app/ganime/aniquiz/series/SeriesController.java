package app.ganime.aniquiz.series;

import org.modelmapper.ModelMapper;
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
	private ModelMapper mapper;

	@GetMapping
	public List<SeriesDTO> getSeries() {
		List<Series> series = seriesService.getSeries();
		return series.stream()
			.map(s -> mapper.map(s, SeriesDTO.class))
			.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long postSeries(@RequestBody SeriesDTO seriesDTO) {
		Series series = mapper.map(seriesDTO, Series.class);
		return seriesService.saveSeries(series).getId();
	}

	@GetMapping("/{id}")
	public SeriesDTO getSeries(@PathVariable("id") Long id) {
		Series series = seriesService.getSeries(id);
		return mapper.map(series, SeriesDTO.class);
	}
}
