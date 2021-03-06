package sumodashboard.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import sumodashboard.model.MetaData;

/**
 * Class used to work with setting Metadata
 */
public class MetaDataIO { 
	
	/**
	 * Read metadata from a metadata.txt file and store in a MetaData object
	 * @param file file containing metadata
	 * @return Metadata object
	 * @throws FileNotFoundException file is not found
	 */
    public static MetaData parseMetadata(File file) throws FileNotFoundException {
		Scanner reader = new Scanner(file);	
		MetaData meta = new MetaData();
		while (reader.hasNextLine()) {
	        String line = reader.nextLine();
	        if(line.contains("Name: "))  meta.setName(line.split(": ")[1]);
	        if(line.contains("Date: ")) meta.setDate(line.split(": ")[1]);
	        if(line.contains("Description: ")) meta.setDescription(line.split("\\s*:\\s*")[1]);	   
	        
	        if(line.contains("Tags: ")) {
	        	ArrayList<String> tagsList = new ArrayList<String>(Arrays.asList(line.split("\\s*:\\s*")[1].split("\\s*;\\s*")));
	        	meta.setTags(generateTagsString(tagsList));
	        }	        
	        
	      }
		reader.close();
		return meta;
    }
    
    /**
     * Convert a list of tags to a single string of all tags with the delimiter specified in class MetaData
     * @param li List<String tag>
     * @return String containing all tags
     */
    public static String generateTagsString(List<String> li) {
    	String tags;
		if (li.size() == 0) {
			tags = "";
		} else {
			tags = li.get(0);
			for (int i = 1; i < li.size(); i++) {
				tags += (MetaData.TAGDELIMITER + li.get(i));
			}
		}
		return tags;
	}
	
	/**
	 * Generates a random ID of size 'length', never starting with a 0 
	 * @param length the length of the required random id
	 * @return int random_id
	 */
	public static int generateId(int length) {
		Random random = new Random();
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString().substring(0, length-1);
		str = String.valueOf(random.nextInt(9) +1) + str.replaceAll("[^0-9.]", String.valueOf((random).nextInt(9) +1));
		System.out.println("New simulation: generated id = " + Integer.parseInt(str));
		return Integer.parseInt(str);
	}
	
	/**
	 * Check if tags exists, if not, create new one. Then add it to 'simulation_tag' table
	 * @param simId simulation id (int)
	 * @param tags tags (String)
	 * @throws SQLException database not reachable
	 */
	public static void addTagsToSimulation(int simId, String tags) throws SQLException {		
		for (String tag : tags.split(MetaData.TAGDELIMITER)) {
			tag = tag.replace(" ", "");
			if (tag.equals("")) continue;
			
			Integer tagId = SimulationDao.getTagId(tag);
			
			if (tagId == null) {
				SimulationDao.createTag(tag);
				tagId = SimulationDao.getTagId(tag);
			} 
			
			SimulationDao.storeSimTag(tagId, simId);
		}
	}
}