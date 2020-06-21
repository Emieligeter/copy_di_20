package sumodashboard.model;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor //Makes a constructor with all variables as parameters
public class SumoFilesDTO {
	private HashMap<String, File> files;
	private TreeMap<Integer, File> stateFiles;

}