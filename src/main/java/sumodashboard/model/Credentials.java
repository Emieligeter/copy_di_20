package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
public class Credentials {
	private String username;
	private String password;
}
