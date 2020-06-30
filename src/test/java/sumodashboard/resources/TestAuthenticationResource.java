package sumodashboard.resources;

import java.net.MalformedURLException;

import java.sql.SQLException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sumodashboard.model.Account;

public class TestAuthenticationResource {

	 private static String localHost = "http://localhost:8080/sumo-dashboard/";

	 private static Client client;
	 private static WebTarget target;
	 private static NewCookie cookie;

	 private static String testUsername = "testUser";
	 private static String testPassword = "z5mc4m*D@&Yv-B43";
	 private static String testEmail = "test@test.test";
	 

	@BeforeAll
	public static void setup() throws MalformedURLException {
		client = ClientBuilder.newClient();
		target = client.target(localHost);
	}

	@AfterAll
	public static void teardown() throws MalformedURLException {
		client.close();
	}
	
	/**
	 * Tests whether login endpoint is working. This tests uses the {@link Client}  object to send 
	 * a request to the URI and check if it returns the right cookie
	 * @throws MalformedURLException
	 */
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
	
	/**
	 * Tests if the user does not receive a cookie when wrong credentials
	 * are entered.
	 * @throws MalformedURLException
	 */
	@Test
	public void TestLoginWrongCredentials() throws MalformedURLException {
		JSONObject jsonCreds = new JSONObject();
		jsonCreds.put("username", testUsername);
		jsonCreds.put("password", "abc");

		WebTarget endPoint = target.path("/rest/auth/login");
		try (Response resAuth = endPoint.request().post(Entity.entity(jsonCreds.toString(), MediaType.APPLICATION_JSON))) {
			cookie = resAuth.getCookies().get("session-id");
			Assertions.assertEquals(403, resAuth.getStatus());
			Assertions.assertNull(cookie);
		}
	}
	
	/**
	 * Tests whether the createUser method is working correctly
	 * This test uses the {@link AuthenticationResource.storeData} variable to ensure no database entry is inserted when testing
	 * @throws SQLException
	 */
	@Test
	public void testCreateUser() throws SQLException {
		AuthenticationResource auth = new AuthenticationResource();
		Account acc = new Account();
		acc.setUsername(testUsername);
		acc.setPassword(testPassword);
		acc.setEmail(testEmail);
		auth.setStoreData(false);
		Response res = auth.createNewUser(acc);
		Assertions.assertEquals(res.getStatus(), 200);
	}
	
	

}
