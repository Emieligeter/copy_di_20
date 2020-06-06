package sumodashboard.dao;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//read file and output json
public class XML2Json {
    static String xml="",str="";
    public static void main(String[] args) throws JSONException, IOException {
        String filepath = "C:\\Users\\joseph\\Documents\\di20-1\\src\\main\\webapp\\WEB-INF\\web.xml";
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        while ((xml = buffer.readLine()) != null) {
            str+=xml;
        }
        JSONObject xml2json = XML.toJSONObject(str);
        System.out.println(xml2json);
    }
}
