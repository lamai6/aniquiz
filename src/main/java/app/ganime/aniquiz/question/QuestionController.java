package app.ganime.aniquiz.question;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

	@Autowired
	private QuestionService service;
	@Autowired
	private ModelMapper mapper;

	@GetMapping
	public List<QuestionDTO> getQuestions() {
		return service.getQuestions()
			.stream()
			.map(q -> mapper.map(q, QuestionDTO.class))
			.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long postQuestions(@RequestBody QuestionDTO questionDTO) {
		return service.saveQuestion(questionDTO).getId();
	}

	@GetMapping("/{id}")
	public QuestionDTO getQuestion(@PathVariable("id") Long id) {
		Question question = service.getQuestion(id);
		return mapper.map(question, QuestionDTO.class);
	}
}
