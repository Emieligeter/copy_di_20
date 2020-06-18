package sumodashboard.model;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

public class SumoFilesDTO {
	private HashMap<String, File> files;
	private TreeMap<Integer, File> stateFiles;

	public SumoFilesDTO(HashMap<String, File> files, TreeMap<Integer, File> stateFiles) {
		this.files = files;
		this.stateFiles = stateFiles;
	}

	public HashMap<String, File> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, File> files) {
		this.files = files;
	}

	public TreeMap<Integer, File> getStateFiles() {
		return stateFiles;
	}

	public void setStateFiles(TreeMap<Integer, File> stateFiles) {
		this.stateFiles = stateFiles;
	}
}