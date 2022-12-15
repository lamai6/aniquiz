package app.ganime.aniquiz.contributor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contributors")
public class ContributorController {

	@Autowired
	private ContributorService service;
	@Autowired
	private ModelMapper mapper;

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping
	public List<ContributorDTO> getContributors() {
		List<Contributor> contributors = service.getContributors();
		return contributors.stream()
			.map(c -> mapper.map(c, ContributorDTO.class))
			.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long postContributor(@Valid @RequestBody ContributorDTO contributorDTO) {
		Contributor contributor = mapper.map(contributorDTO, Contributor.class);
		return service.saveContributor(contributor).getId();
	}

	@PreAuthorize("principal.id == #id")
	@GetMapping("/{id}")
	public ContributorDTO getContributor(@PathVariable("id") Long id) {
		Contributor contributor = service.getContributor(id);
		return mapper.map(contributor, ContributorDTO.class);
	}
}
