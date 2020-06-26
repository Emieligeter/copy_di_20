package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for serializing account data
 */
@Getter
@Setter
@XmlRootElement
public class Account {
	private String username;
	private String password;
	private String email;
	
}
