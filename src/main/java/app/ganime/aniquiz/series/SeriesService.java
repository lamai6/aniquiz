package app.ganime.aniquiz.series;

import app.ganime.aniquiz.config.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {

	@Autowired
	private SeriesRepository repository;

	public Series getSeries(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}

	public List<Series> getSeries() {
		return repository.findAll();
	}

	public Series saveSeries(Series series) {
		return repository.save(series);
	}
}
