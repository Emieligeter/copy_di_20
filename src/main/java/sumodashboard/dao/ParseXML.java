package sumodashboard.dao;

import sumodashboard.model.Configuration;
import sumodashboard.model.Net;
import sumodashboard.model.Routes;
import sumodashboard.model.State;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ParseXML {
    //Takes the simulation.sumocfg file and creates a Configuration object with net and routes set to null.
    private static Configuration parseConfigFile(String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Configuration config = (Configuration) jaxbUnmarshaller.unmarshal(new File(path));
        return config;
    }

    //Takes the net.net.xml file and creates a Net object.
    private static Net parseNetFile(String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Net.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Net net = (Net) jaxbUnmarshaller.unmarshal(new File(path));
        return net;
    }

    //Takes the routes.rou.xml file and creates a Routes object.
    private static Routes parseRoutesFile(String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Routes.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Routes routes = (Routes) jaxbUnmarshaller.unmarshal(new File(path));
        return routes;
    }

    //Parses Routes, Net and Simulation.sumocfg and turns them into one complete Configuration object.
    public static Configuration parseConfigFromFiles(String netPath, String configPath, String routesPath) throws JAXBException {
        Configuration config = parseConfigFile(configPath);
        config.setRoutes(parseRoutesFile(routesPath));
        config.setNet(parseNetFile(netPath));
        return config;
    }

    //Parses a state xml file into a State object.
    public static State parseStateFile(String path) throws JAXBException {
    	JAXBContext jaxbContext = JAXBContext.newInstance(State.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        State state = (State) jaxbUnmarshaller.unmarshal(new File(path));
        return state;
    }
}