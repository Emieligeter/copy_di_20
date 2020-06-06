package sumodashboard.model;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SumoFilesDTO {
	private HashMap<String, File> files = new HashMap<String, File>();
	private TreeMap<Integer, File> stateFiles = new TreeMap<Integer, File>();


	public SumoFilesDTO(HashMap<String, File> files2, TreeMap<Integer, File> stateFiles2) {
		this.files = files2;
		this.stateFiles = stateFiles2;
	}

	public HashMap<String, File> getFiles() {
		return this.files;
	}
	
	public TreeMap<Integer, File> getStateFiles() {
		return this.stateFiles;
	}

}