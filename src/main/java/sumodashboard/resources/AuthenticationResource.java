package sumodashboard.resources;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.annotation.Priority;
import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import sumodashboard.dao.AccountDAO;
import sumodashboard.model.Account;
import sumodashboard.model.Credentials;

@Path("/auth")
public class AuthenticationResource {
	private boolean storeData = true;
	private AccountDAO accountDAO = new AccountDAO();
	private Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogin(Credentials creds) {
		String username = creds.getUsername();
		String password = creds.getPassword();
		try {
			authenticate(username, password);			
			return Response.ok(createToken(username)).build();
		}
		 catch(Exception e) {
			 e.printStackTrace();
			 return Response.status(Response.Status.FORBIDDEN).build();
		 }
	}

	private String createToken(String userName) throws Exception {
		// TODO Auto-generated method stub
		return "12345";
	}

	private void authenticate(String username, String password) throws Exception {
		String hashedPass = accountDAO.getHashedPassword(username);
		boolean passMatch = argon2.verify(hashedPass, password);
		if(!passMatch) throw new AuthenticationException("Password incorrect");
	}
	
	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(Account acc) throws SQLException {
		String username = acc.getUsername();
		String password = acc.getPassword();
		String email = acc.getEmail();
		String hashedPass = argon2.hash(4, 1024 * 1024, 8, password);
		try {
		if(storeData) accountDAO.createNewUser(username, hashedPass, email);
		} catch(SQLException e) {			
			String respMessage ="Unkown error occured, try again later";
			if(e.getMessage().contains("unique constraint \"account_email_key\"")) {
				respMessage = "Email adress already in use";
			} else if(e.getMessage().contains("unique constraint \"account_username_key\"")) {
				respMessage = "Username already in use";
			}
			return Response.status(Response.Status.CONFLICT).entity(respMessage).build();
		}
		if(!storeData) Response.ok("User created but not stored").build();
		return Response.ok("User created succesfully").build();
	}
	
	
	

}
