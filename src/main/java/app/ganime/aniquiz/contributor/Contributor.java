package app.ganime.aniquiz.contributor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Contributor {

	private Long id;
	private String username;
	private String email;
	private String password;
}
