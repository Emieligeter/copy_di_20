package sumodashboard.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import sumodashboard.dao.ParseXML;
import sumodashboard.model.Configuration;
import sumodashboard.model.Net;
import sumodashboard.model.Routes;
import sumodashboard.model.Simulation;
import sumodashboard.model.State;

public class FileReadService {

	public static final String tmpFolder = System.getProperty("java.io.tmpdir");

	public FileReadService() {
	}

	public void readInputStream(InputStream stream, FormDataBodyPart bodyPart, List<String> fileList) throws Exception {

		if (fileList.size() == 1 && fileList.get(0).contains(".zip")) {
			handleZip(convertToZipStream(stream));
		} else {
			checkFileList(fileList);
			handleFiles(stream, bodyPart);
		}
		stream.close();
	}

	public void handleZip(ZipInputStream zipStream) throws Exception {
		List<File> files = new ArrayList<File>();
		List<File> stateFiles = new ArrayList<File>();

		ZipEntry entry;

		while ((entry = zipStream.getNextEntry()) != null) {

			// Get the file names
			String longFileName = entry.getName();
			String[] splitFileFolder = (longFileName.split("/"));
			String fileName = splitFileFolder[splitFileFolder.length - 1];

			// Set file location
			String fileLocation = tmpFolder + '/' + fileName;

			// convert stream to files and store in array
			if (fileName.equals("state.zip")) {
				System.out.println("Unzipping: " + fileName + " to: " + fileLocation);
				stateFiles = convertStateFiles(zipStream, fileLocation);
			} else {
				System.out.println("Unzipping: " + fileName + " to: " + fileLocation);
				files.add(convertStreamToFile(zipStream, fileLocation));
			}
		}
		zipStream.close();
		convertFilesToSumoSimulation(files, stateFiles);
	}

	// TODO Combine methods above and below
	public void handleFiles(InputStream stream, FormDataBodyPart bodyParts) throws Exception {
		List<File> files = new ArrayList<File>();
		List<File> stateFiles = new ArrayList<File>();

		for (BodyPart part : bodyParts.getParent().getBodyParts()) {
			InputStream inputStream = part.getEntityAs(InputStream.class);

			// Get the file names
			String longFileName = part.getContentDisposition().getFileName();
			String[] splitFileFolder = longFileName.split("/");
			String fileName = splitFileFolder[splitFileFolder.length - 1];

			// Set file location
			String fileLocation = tmpFolder + '/' + fileName;

			// Convert stream to files and store in array
			if (!fileName.equals("")) {
				if (fileName.equals("state.zip")) {
					System.out.println("Converting: " + fileName + " to: " + fileLocation);
					stateFiles = convertStateFiles(inputStream, fileLocation);
				} else {
					System.out.println("Converting: " + fileName + " to: " + fileLocation);
					files.add(convertStreamToFile(inputStream, fileLocation));
				}
			}
		}

		stream.close();
		convertFilesToSumoSimulation(files, stateFiles);
	}
	
	
	private void convertFilesToSumoSimulation(List<File> files, List<File> stateFiles) throws Exception {
		//Create empty classes
		Simulation simulation = new Simulation();
		List<State> states = new ArrayList<>();
		Configuration config = null;
		Routes routes = null;
		Net net = null;
		
		//Fill class with data from files
		for (File f : files) {
			switch (f.getName()) {
			case "routes.rou.xml":
				routes = ParseXML.parseRoutesFile(f);
				break;
			case "net.net.xml":
				net = ParseXML.parseNetFile(f);
				break;
			case "simulation.sumocfg":
				config = ParseXML.parseConfigFile(f);
				break;
			case "metadata.txt":
				simulation = ParseXML.parseMetadata(f, simulation);
				break;
			}
		}
		for (File sf : stateFiles) {
			states.add(ParseXML.parseStateFile(sf));
		}

		//Delete files after use
		files.forEach(f -> f.delete());
		stateFiles.forEach(sf -> sf.delete());

	}

	public void checkFileList(List<String> fileList) throws IOException {
		// TODO Checks on correctness of files
	}

	private File convertStreamToFile(InputStream is, String fileNameLocation) throws Exception {
		int size;
		byte[] buffer = new byte[2048];
		FileOutputStream fos = new FileOutputStream(fileNameLocation);
		BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
		while ((size = is.read(buffer, 0, buffer.length)) != -1) {
			bos.write(buffer, 0, size);
		}
		bos.flush();
		bos.close();
		return new File(fileNameLocation);
	}

	private List<File> convertStateFiles(InputStream is, String fileLocation) throws Exception {
		List<File> stateFiles = new ArrayList<File>();
		ZipInputStream stateZip = new ZipInputStream(is);
		ZipEntry entry;

		while ((entry = stateZip.getNextEntry()) != null) {
			String longFileName = entry.getName();
			String[] splitFileFolder = (longFileName.split("/"));
			String fileName = splitFileFolder[splitFileFolder.length - 1];
			// System.out.println("Unzipping: " + fileName);

			stateFiles.add(convertStreamToFile(stateZip, fileName));
		}
		return stateFiles;
	}

	public ZipInputStream convertToZipStream(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ZipInputStream zipStream = new ZipInputStream(bis);
		return new ZipInputStream(bis);
	}
}
