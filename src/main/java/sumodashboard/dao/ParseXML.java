package sumodashboard.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sumodashboard.model.MetaData;

public class ParseXML {    
    public static MetaData parseMetadata(File file) throws FileNotFoundException, ParseException {
		Scanner reader =  new Scanner(file);	
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
    
    public static String generateTagsString(List<String> li) {
    	String tags;
		if (li.size() == 0) {
			tags = "";
		}
		else {
			tags = li.get(0);
			for (int i = 1; i < li.size(); i++) {
				tags += (MetaData.TAGDELIMITER + li.get(i));
			}
		}
		
		return tags;
	}
}