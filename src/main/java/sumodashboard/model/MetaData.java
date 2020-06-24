package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Object containing all metadata for a simulation
 */
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MetaData {
	public static final String TAGDELIMITER = ", ";
	
	private int ID;
	private String name;
	private String date;
	private String description;
	private String researcher;
	private String tags;
}
