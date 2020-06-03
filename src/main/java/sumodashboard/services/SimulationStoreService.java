package sumodashboard.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import sumodashboard.dao.ParseXML;
import sumodashboard.dao.SimulationDao;
import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

public class SimulationStoreService {

	public static void storeSimulation(HashMap<String, File> files, TreeMap<Integer, File> states) throws Exception {
		// Generate random ID
		
		
		//parse metadata into object
		MetaData meta = ParseXML.parseMetadata(files.get("metadata.txt"));
		
		int simId;
		do {simId = generateId(5);
		}while(SimulationDao.instance.doesSimIdExist(simId)) ;
		 
		//Store a simulation in 'simulation' table
		SimulationDao.instance.storeSimulation(
				simId, meta.getName(), 
				meta.getDescription(), 
				meta.getDate(),
				files.get("net.net.xml"), 
				files.get("routes.rou.xml"), 
				files.get("simulation.sumocfg"));

		//Store all state files in 'states' table
		for (Map.Entry<Integer, File> sf : states.entrySet()) {
			Integer timeStamp = sf.getKey();
			File file = sf.getValue();
			SimulationDao.instance.storeState(simId, timeStamp, file);
		}
		
		//Check if tags exists, if not, create new one. If it does add it to 'simulation_tag' table
		for(String tag : meta.getTags()) {
			Integer tagId = SimulationDao.instance.getTagId(tag);
			if(tagId == null) {
				tagId = generateId(4);
				SimulationDao.instance.storeTag(tagId, tag);				
			} 
			SimulationDao.instance.storeSimTag(tagId, simId);
		}
		
		// Delete files after use
		files.forEach((key, file) -> file.delete());
		states.forEach((key, file) -> file.delete());
	}

	public static void storeRoutes(File file, String simID) {

	}
	
	//Generates a random ID of size 'length', never starting with a 0 
	private static int generateId(int length) {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString().substring(0, length-1);
		str = String.valueOf((new Random()).nextInt(9) +1) + str.replaceAll("[^0-9.]", String.valueOf((new Random()).nextInt(9) +1));
		return Integer.parseInt(str);
	}
}
