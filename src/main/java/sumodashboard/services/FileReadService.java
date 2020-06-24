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

/**
 * Read uploaded files and store the data in a SumoFilesDTO
 */
public class FileReadService {

	public static final String tmpFolder = System.getProperty("java.io.tmpdir");

	public FileReadService() {
	}
	
	/**
	 * The {@link InputStream} is checked to see it is a ZIP file. If it is, then the stream is used in {@link #handleZip()}. 
	 * If it's not a zip the stream and {@link FormDataBodyPart} are used in {@link #handleFiles()}. 
	 * @param stream
	 * @param bodyPart
	 * @return {@link SumoFilesDTO}
	 * @throws Exception
	 */
	public static SumoFilesDTO readInputStream(InputStream stream, FormDataBodyPart bodyPart ) throws Exception {
		List<String> fileList = getFileList(bodyPart);
		
		if (fileList.size() == 1 && fileList.get(0).contains(".zip")) {
			return handleZip(convertToZipStream(stream));
		} else {
			checkFileList(fileList);		
			return handleFiles(stream, bodyPart);
		}
	}
	
	/**
	 * The {@link ZipInputStream} is parsed into two {@link HashMap}s. One for the files, one for the state files.
	 * These are then stored into a {@link SumoFilesDTO}.
	 * @param zipStream
	 * @return {@link SumoFilesDTO}
	 * @throws Exception
	 */
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
				stateFiles = convertStateFiles(zipStream, fileLocation);
			} else {
				files.put(fileName, convertStreamToFile(zipStream, fileLocation));
			}
		}
		zipStream.close();
		

		return new SumoFilesDTO(files, stateFiles);
		//SimulationStoreService.storeSimulation(files, stateFiles);
		
	}
	
	/**
	 * Generate a DTO for individual files
	 * @param stream input stream
	 * @param bodyParts FormDataBodyPart
	 * @return SumoFilesDTO
	 * @throws Exception
	 */
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

			// Convert stream to files and store in array
			if (!fileName.equals("")) {
				if (fileName.equals("state.zip")) {
					stateFiles = convertStateFiles(inputStream, fileLocation);
				} else {
					files.put(fileName, convertStreamToFile(inputStream, fileLocation));
				}
			}
		}

		stream.close();

		return new SumoFilesDTO(files, stateFiles);
	}
	
	/**
	 * Check if the uploaded files are correct
	 * @param fileList List<String file>
	 * @throws IOException
	 */
	public static void checkFileList(List<String> fileList) throws IOException {
		// TODO Checks on correctness of files
	}
	
	/**
	 * Convert an InputStream to a File
	 * @param is input stream
	 * @param fileNameLocation location of created file (String)
	 * @return File
	 * @throws Exception
	 */
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
	
	/**
	 * Unpack the zip with state files into a Map of files
	 * @param is input stream
	 * @param fileLocation location of created file (String)
	 * @return TreeMap<Integer, File>
	 * @throws Exception
	 */
	private static TreeMap<Integer, File> convertStateFiles(InputStream is, String fileLocation) throws Exception {
		TreeMap<Integer, File> stateFiles = new TreeMap<Integer, File>();
		ZipInputStream stateZip = new ZipInputStream(is);
		ZipEntry entry;

		while ((entry = stateZip.getNextEntry()) != null) {
			String longFileName = entry.getName();
			String[] splitFileFolder = (longFileName.split("/"));
			String fileName = splitFileFolder[splitFileFolder.length - 1];
			
			String fileNumbers = fileName.replaceAll("[^0-9.]", "");
			String[] splitTimeStamp = fileNumbers.split("\\.");
			int timeStamp = Integer.parseInt(splitTimeStamp[0]);
			stateFiles.put(timeStamp, convertStreamToFile(stateZip, (tmpFolder + "/" + fileName)));
		}
		return stateFiles;
	}
	
	/**
	 * Get the list of files given in the BodyPart
	 * @param bodyPart BodyPart
	 * @return List<String file>
	 */
	public static List<String> getFileList(BodyPart bodyPart){
		List<String> fileList = new ArrayList<String>();
		for (BodyPart part : bodyPart.getParent().getBodyParts()) {
			ContentDisposition meta = part.getContentDisposition();
			if(meta.getFileName().length() > 3) fileList.add('\n' + meta.getFileName());
		}
		return fileList;
	}
	
	/**
	 * Convert the InputStream to a ZipInputStream to handle file reading for a zip file
	 * @param stream input stream
	 * @return ZipInputStream
	 * @throws IOException
	 */
	public static ZipInputStream convertToZipStream(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ZipInputStream zipStream = new ZipInputStream(bis);
		return zipStream;
	}
}
