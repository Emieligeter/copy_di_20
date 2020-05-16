package sumodashboard.dao;

import sumodashboard.model.Edge;
import sumodashboard.model.Lane;
import sumodashboard.model.Net;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ParseXML {
    public static Net parseNetFile() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Net.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object obj = jaxbUnmarshaller.unmarshal(new File("C:\\Users\\Reijer\\Downloads\\SUMO example 2\\net.net.xml"));
            System.out.println(obj);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        ParseXML.parseNetFile();
    }
}