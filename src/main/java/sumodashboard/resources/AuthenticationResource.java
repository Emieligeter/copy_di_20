package sumodashboard.resources;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import sumodashboard.dao.AccountDao;
import sumodashboard.model.Account;
import sumodashboard.model.Credentials;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Path("/auth")
public class AuthenticationResource {
	@Context
	ContainerRequestContext requestContext;
	
	private boolean storeData = true;
	private Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
	private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final String API_TOKEN = "ZVXTyfmKXb7FxngTEAq2DHVmXZCxecJWTQLDsDnEce3dzhVK";
    
    private static final String MASTER_PASSWORD = "ZhXg36#8!";
    
    private static final int SECONDS_UNTIL_AUTOMATIC_SIGNOUT = 2*60*60; //2 hours

	/**
	 * Login endpoint. A username and password are received as a json and serialized as {@link Credentials}.
	 * These credentials are used to authenticate the user
	 * If the authentication is successful a {@link NewCookie} is created with a {@link JWT} as body
	 * @param creds credentials of the user in 
	 * @return Response
	 */
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogin(Credentials creds) {
		String username = creds.getUsername();
		
		String password = creds.getPassword();
		
		try {
			authenticate(username, password);	
			String token = createToken(username);
			NewCookie cookie = new NewCookie("session-id",token , "/", null, null, SECONDS_UNTIL_AUTOMATIC_SIGNOUT, false, true);
			System.out.println("Token created: " + token);
			return Response.ok("login successful").cookie(cookie).build();
		} catch(AuthenticationException e) {
			return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
		} catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	/**
	 * When this endpoint is called a {@link Response} is sent with a {@link NewCookie} with name "session-id"
	 * This overwrites the valid {@link JWT} so the user can't reach the other enpoints anymore
	 * @return Response
	 */
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogout() {
		try {
			NewCookie cookie = new NewCookie("session-id","" , "/", null, null, SECONDS_UNTIL_AUTOMATIC_SIGNOUT, true, true);
			System.out.println(SECONDS_UNTIL_AUTOMATIC_SIGNOUT);
			return Response.ok("logout successful").cookie(cookie).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	/**
	 * First gets the hashed password associated with the username. Then uses {@link Argon2} algorithm
	 * to very the provided password with the hashed password
	 * @param username
	 * @param password
	 * @throws AuthenticationException
	 * @throws SQLException
	 */
	private void authenticate(String username, String password) throws AuthenticationException, SQLException {
		String hashedPass = AccountDao.getHashedPassword(username);
		boolean passMatch = argon2.verify(hashedPass, password);
		if(!passMatch) throw new AuthenticationException("Password incorrect");
	}

	/**
	 * Creates a {@link JWT} using the username. Uses {@link HMAC256} {@link Algorithm}
	 * with {@link SECRET_KEY} and certain expiry and issuer parameters
	 * @param username
	 * @return {@link JWT} as string, null if an exception occurs
	 */
	private static String createToken(String username ) {
		try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusSeconds(SECONDS_UNTIL_AUTOMATIC_SIGNOUT).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create()
                    .withIssuedAt(issuedAt) 
                    .withExpiresAt(expirationDate) 
                    .withClaim("username", username) 
                    .withIssuer("sumoDashboard") 
                    .sign(algorithm); 
        } catch (JWTCreationException e) {
           	e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * Check if token is valid and if the user exists in the database
	 * @param token
	 * @return boolean: is it valid?
	 */
	private static boolean validateToken(String token) {
		try {
			if (token != null) {
	            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
	            JWTVerifier verifier = JWT.require(algorithm)
	                    .withIssuer("sumoDashboard")
	                    .build(); //Reusable verifier instance
	            DecodedJWT jwt = verifier.verify(token);
	            //Get the userId from token claim.
	            String username = jwt.getClaim("username").asString();
	            AccountDao.getUserByName(username);
	            
	            return true;
			}
	    } catch (JWTVerificationException e){
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	    return false;
	}
	
	/**
	 * Check if a rest request with a token is authorized
	 * @param requestContext
	 * @return boolean: is it authorized?
	 */
	public static boolean isAuthorized(ContainerRequestContext requestContext) {
		//First check authorization header
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization != null && authorization.startsWith(AUTHENTICATION_SCHEME + " ")) {
            String token = authorization.substring(AUTHENTICATION_SCHEME.length()).trim();
            return (token.equals(API_TOKEN));
        }
        
        //If no bearer token has been submitted, check cookies
		Map<String, Cookie> cookies = requestContext.getCookies();
        if(cookies.get("session-id") != null) {
        	return validateToken(cookies.get("session-id").getValue());
        }
        return false;
	}
	
	/**
	 * Creates a user based on the {@link Account} parameters. The {@link Argon2} algorithm is used to hash the password with a salt
	 * The user is stored through the {@link AccountDao} class
	 * @param acc
	 * @return Resonse
	 * @throws SQLException
	 */
	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(Account acc) throws SQLException {		
		String username = acc.getUsername();
		String password = acc.getPassword();
		String email = acc.getEmail();
		String master = acc.getMasterPassword();
		checkMasterPassword(master);
		String hashedPass = argon2.hash(4, 128 * 1024, 8, password);
		try {
		if(storeData) AccountDao.createNewUser(username, hashedPass, email);
		} catch(SQLException e) {			
			String respMessage ="Unkown error occured, try again later";
			if(e.getMessage().contains("unique constraint \"account_email_key\"")) {
				respMessage = "Email adress already in use";
			} else if(e.getMessage().contains("unique constraint \"account_username_key\"")) {
				respMessage = "Username already in use";
			}
			e.printStackTrace();
			return Response.status(Response.Status.CONFLICT).entity(respMessage).build();
		}
		if(!storeData) Response.ok("User created but not stored").build();
		return Response.ok("User created succesfully").build();
	}
	
	private void checkMasterPassword(String master) throws SQLException {
		if(!master.equals(MASTER_PASSWORD)) throw new SQLException("Master password incorrect");
	}

	//These setters and getters are used in the test class
	public void setStoreData(boolean storeData) {
		this.storeData = storeData;
	}
	public String getApiToken() {
		return API_TOKEN;
	}
	public String getAuthenticationScheme() {
		return AUTHENTICATION_SCHEME;
	}

}
