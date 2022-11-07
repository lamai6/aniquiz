package app.ganime.aniquiz.question;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

	@Autowired
	private QuestionService service;
	@Autowired
	private ModelMapper mapper;

	@GetMapping("/{id}")
	public QuestionDTO getQuestion(@PathVariable("id") Long id) {
		Question question = service.getQuestion(id);
		return mapper.map(question, QuestionDTO.class);
	}
}
