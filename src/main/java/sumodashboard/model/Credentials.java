package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for serializing credentials when user logs in
 *
 */
@XmlRootElement
@Getter
@Setter
public class Credentials {
	private String username;
	private String password;
}
