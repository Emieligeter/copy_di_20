package sumodashboard.model;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

//TODO figure out what DTO means
public class SumoFilesDTO {
	private HashMap<String, File> files;
	private TreeMap<Integer, File> stateFiles;

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