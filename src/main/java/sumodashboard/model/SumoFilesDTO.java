package sumodashboard.model;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//This creates the get and set methods for all variables
@Getter
@Setter
@AllArgsConstructor //Makes a constructor with all variables as parameters
public class SumoFilesDTO {
	private HashMap<String, File> files;
	private TreeMap<Integer, File> stateFiles;
}