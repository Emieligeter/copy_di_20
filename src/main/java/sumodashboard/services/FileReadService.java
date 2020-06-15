package sumodashboard.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import sumodashboard.model.SumoFilesDTO;

public class FileReadService {

	public static final String tmpFolder = System.getProperty("java.io.tmpdir");

	public FileReadService() {
	}

	public static SumoFilesDTO readInputStream(InputStream stream, FormDataBodyPart bodyPart ) throws Exception {
		List<String> fileList = getFileList(bodyPart);
		
		if (fileList.size() == 1 && fileList.get(0).contains(".zip")) {
			return handleZip(convertToZipStream(stream));
		} else {
			checkFileList(fileList);		
			return handleFiles(stream, bodyPart);
		}
	}

	private static SumoFilesDTO handleZip(ZipInputStream zipStream) throws Exception {
		HashMap<String, File> files = new HashMap<String, File>();
		TreeMap<Integer, File> stateFiles = new TreeMap<Integer, File>();

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
				files.put(fileName, convertStreamToFile(zipStream, fileLocation));
			}
		}
		zipStream.close();
		

		return new SumoFilesDTO(files, stateFiles);
		//SimulationStoreService.storeSimulation(files, stateFiles);
		
	}

	private static SumoFilesDTO handleFiles(InputStream stream, FormDataBodyPart bodyParts) throws Exception {
		HashMap<String, File> files = new HashMap<String, File>();
		TreeMap<Integer, File> stateFiles = new TreeMap<Integer, File>();
		
		for (BodyPart part : bodyParts.getParent().getBodyParts()) {
			InputStream inputStream = part.getEntityAs(InputStream.class);

			// Get the file names
			String longFileName = part.getContentDisposition().getFileName();
			String[] splitFileFolder = longFileName.split("/");
			String fileName = splitFileFolder[splitFileFolder.length - 1];

			// Set file location
			String fileLocation = tmpFolder + '/' + fileName;
			System.out.println("FileLocation: " + fileLocation);

			// Convert stream to files and store in array
			if (!fileName.equals("")) {
				if (fileName.equals("state.zip")) {
					System.out.println("Converting: " + fileName + " to: " + fileLocation);
					stateFiles = convertStateFiles(inputStream, fileLocation);
				} else {
					System.out.println("Converting: " + fileName + " to: " + fileLocation);
					files.put(fileName, convertStreamToFile(inputStream, fileLocation));
				}
			}
		}

		stream.close();

		return new SumoFilesDTO(files, stateFiles);
	}
	

	public static void checkFileList(List<String> fileList) throws IOException {
		// TODO Checks on correctness of files
	}

	private static File convertStreamToFile(InputStream is, String fileNameLocation) throws Exception {
		int size;
		byte[] buffer = new byte[2048];
		new File(fileNameLocation).createNewFile();
		FileOutputStream fos = new FileOutputStream(fileNameLocation);
		BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
		while ((size = is.read(buffer, 0, buffer.length)) != -1) {
			bos.write(buffer, 0, size);
		}
		bos.flush();
		bos.close();
		return new File(fileNameLocation);
	}

	private static TreeMap<Integer, File> convertStateFiles(InputStream is, String fileLocation) throws Exception {
		TreeMap<Integer, File> stateFiles = new TreeMap<Integer, File>();
		ZipInputStream stateZip = new ZipInputStream(is);
		ZipEntry entry;

		while ((entry = stateZip.getNextEntry()) != null) {
			String longFileName = entry.getName();
			String[] splitFileFolder = (longFileName.split("/"));
			String fileName = splitFileFolder[splitFileFolder.length - 1];
			System.out.println("Unzipping: " + fileName);			
			
			String fileNumbers = fileName.replaceAll("[^0-9.]", "");
			String[] splitTimeStamp = fileNumbers.split("\\.");
			int timeStamp = Integer.parseInt(splitTimeStamp[0]);
			stateFiles.put(timeStamp, convertStreamToFile(stateZip, (tmpFolder + "/" + fileName)));
		}
		return stateFiles;
	}
	
	public static List<String> getFileList(BodyPart bodyPart){
		List<String> fileList = new ArrayList<String>();
		for (BodyPart part : bodyPart.getParent().getBodyParts()) {
			ContentDisposition meta = part.getContentDisposition();
			if(meta.getFileName().length() > 3) fileList.add('\n' + meta.getFileName());
		}
		return fileList;
	}
	public static ZipInputStream convertToZipStream(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ZipInputStream zipStream = new ZipInputStream(bis);
		return new ZipInputStream(bis);
	}
}
