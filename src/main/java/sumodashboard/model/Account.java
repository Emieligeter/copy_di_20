package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
@AllArgsConstructor
public class Account {
	private String username;
	private String password;
	private String email;
}
