package sumodashboard.resources;

import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import sumodashboard.dao.AccountDAO;

@Path("/auth")
public class AuthenticationResource {
	private boolean storeData = false;
	private AccountDAO accountDAO = new AccountDAO();
	private Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response userLogin(@FormParam("inputUsername") String userName, @FormParam("inputPassword") String password) {
		try {
			authenticate(userName, password);			
			return Response.ok(createToken(userName)).build();
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createNewUser(@FormParam("username")String username, @FormParam("password") String password, @FormParam("email") String email) throws SQLException {
		String hashedPass = argon2.hash(4, 1024 * 1024, 8, password);
		if(storeData) accountDAO.createNewUser(username, hashedPass, email);
		return Response.ok("User created succesfully").build();
	}
	

}
