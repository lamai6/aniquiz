package app.ganime.aniquiz.contributor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorDTO {

	private String username;
	@Email(message = "{contributor.email.invalid}")
	private String email;
	private String password;
	@JsonProperty("created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;
}
