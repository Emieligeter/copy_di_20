package sumodashboard.resources;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

public class TestAuthenticationResource {

	 private static String localHost = "http://localhost:8080/sumo-dashboard/";

	 private static Client client;
	 private static WebTarget target;
	 private static NewCookie cookie;

	 private static String testUsername = "testUser";
	 private static String testPassword = "z5mc4m*D@&Yv-B43";

	 

	@BeforeAll
	public static void setup() throws MalformedURLException {
		client = ClientBuilder.newClient();
		target = client.target(localHost);
	}

	@AfterAll
	public static void teardown() throws MalformedURLException {
		client.close();
	}

	@Test
	public void TestLogin() throws MalformedURLException {
		JSONObject jsonCreds = new JSONObject();
		jsonCreds.put("username", testUsername);
		jsonCreds.put("password", testPassword);

		WebTarget endPoint = target.path("/rest/auth/login");
		try (Response resAuth = endPoint.request().post(Entity.entity(jsonCreds.toString(), MediaType.APPLICATION_JSON))) {
			cookie = resAuth.getCookies().get("session-id");
			Assertions.assertEquals(200, resAuth.getStatus());
			Assertions.assertTrue(cookie != null);
		}
	}
	
	

}
