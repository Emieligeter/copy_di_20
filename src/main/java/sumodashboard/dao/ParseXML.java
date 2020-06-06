package sumodashboard.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import sumodashboard.model.Configuration;
import sumodashboard.model.MetaData;
import sumodashboard.model.Net;
import sumodashboard.model.Routes;
import sumodashboard.model.Simulation;
import sumodashboard.model.State;

public class ParseXML {
    //Takes the simulation.sumocfg file and creates a Configuration object with net and routes set to null.
	public static Configuration parseConfigFile(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Configuration config = (Configuration) jaxbUnmarshaller.unmarshal(file);
        return config;
    }

    //Takes the net.net.xml file and creates a Net object.
    public static Net parseNetFile(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Net.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Net net = (Net) jaxbUnmarshaller.unmarshal(file);
        return net;
    }
    
    public static MetaData parseMetadata(File file) throws FileNotFoundException, ParseException {
		Scanner reader =  new Scanner(file);	
		MetaData meta = new MetaData();
		while (reader.hasNextLine()) {
	        String line = reader.nextLine();
	        if(line.contains("Name: "))  meta.setName(line.split(": ")[1]);
	        if(line.contains("Date: ")) meta.setDate(new SimpleDateFormat("MMMMM dd, yyyy").parse(line.split(": ")[1]));
	        if(line.contains("Tags: ")) meta.setTags(new ArrayList<String>(Arrays.asList(line.split("\\s*:\\s*")[1].split("\\s*;\\s*"))));
	        if(line.contains("Description: ")) meta.setDescription(line.split("\\s*:\\s*")[1]);	   
	      }
		reader.close();
		return meta;

    }
    //Takes the routes.rou.xml file and creates a Routes object.
    public static Routes parseRoutesFile(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Routes.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Routes routes = (Routes) jaxbUnmarshaller.unmarshal(file);
        return routes;
    }

    //Parses Routes, Net and Simulation.sumocfg and turns them into one complete Configuration object.
    public static Configuration parseConfigFromFiles(File netFile, File configFile, File routesFile) throws JAXBException {
        Configuration config = parseConfigFile(configFile);
        config.setRoutes(parseRoutesFile(routesFile));
        config.setNet(parseNetFile(netFile));
        return config;
    }

    //Parses a state xml file into a State object.
    public static State parseStateFile(File file) throws JAXBException {
    	JAXBContext jaxbContext = JAXBContext.newInstance(State.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        State state = (State) jaxbUnmarshaller.unmarshal(file);
        return state;
    }
}