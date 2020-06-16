package sumodashboard.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
	        if(line.contains("Tags: ")) meta.setTags(new ArrayList<String>(Arrays.asList(line.split("\\s*:\\s*")[1].split("\\s*;\\s*"))));
	        if(line.contains("Description: ")) meta.setDescription(line.split("\\s*:\\s*")[1]);	   
	      }
		reader.close();
		return meta;
    }
}