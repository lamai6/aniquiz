package app.ganime.aniquiz.title;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class TitleId implements Serializable {
	private Long questionId;
	@Column(name = "language_id")
	private Long languageId;
}
