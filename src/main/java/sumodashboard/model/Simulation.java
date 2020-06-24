package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Simulation object, contains the metadata, net file, routes file and configuration file
 */
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Simulation {
	private int ID;
	private String name;
	private String date;
	private String description;
	private String researcher;
	private String tags;
	
	@ToString.Exclude private String net;
	@ToString.Exclude private String routes;
	@ToString.Exclude private String config;
}