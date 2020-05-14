package sumodashboard.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.w3c.dom.Document;

import sumodashboard.model.Simulation;

public class FileReadService {

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
		Path cachePath = Paths.get("/home/emiel/My Files/Studie/Project/Zips");
		List<File> files = new ArrayList<File>();

		ZipEntry entry;

		while ((entry = zipStream.getNextEntry()) != null) {
			System.out.println("Unzipping: " + entry.getName());

			int size;
			byte[] buffer = new byte[2048];

			String[] fileNames = entry.getName().split("/");
			String fileLocation = cachePath.toString() + '/' + fileNames[fileNames.length - 1];

			FileOutputStream fos = new FileOutputStream(fileLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

			while ((size = zipStream.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, size);
			}
			files.add(new File(fileLocation));

			bos.flush();
			bos.close();
		}
		zipStream.close();
		convertFilesToSumoSimulation(files);
	}

	// TODO Combine methods above and below
	public void handleFiles(InputStream stream, FormDataBodyPart bodyParts) throws Exception {
		Path cachePath = Paths.get("/home/emiel/My Files/Studie/Project/Files");
		List<File> files = new ArrayList<File>();

		for (BodyPart part : bodyParts.getParent().getBodyParts()) {
			String[] fileNames = part.getContentDisposition().getFileName().split("/");
			String fileName = fileNames[fileNames.length - 1];
			if (!fileName.equals("")) {
				System.out.println("Converting: " + fileName);

				InputStream is = part.getEntityAs(InputStream.class);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = factory.newDocumentBuilder();

				int size;
				byte[] buffer = new byte[2048];

				String fileLocation = cachePath.toString() + '/' + fileName;

				FileOutputStream fos = new FileOutputStream(fileLocation);
				BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

				while ((size = is.read(buffer, 0, buffer.length)) != -1) {
					bos.write(buffer, 0, size);

				}
				files.add(new File(fileLocation));
				bos.flush();
				bos.close();
				is.close();
			}
		}
		stream.close();
		convertFilesToSumoSimulation(files);
	}

	private void convertFilesToSumoSimulation(List<File> files) {
		Simulation sim = new Simulation();

		files.forEach(f -> System.out.println("converted: " + f.getName()));
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(files.get(1));
			System.out.println(doc.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkFileList(List<String> fileList) throws IOException {
		// TODO Checks on correctness of files
	}

	public ZipInputStream convertToZipStream(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ZipInputStream zipStream = new ZipInputStream(bis);
		return new ZipInputStream(bis);
	}
}
