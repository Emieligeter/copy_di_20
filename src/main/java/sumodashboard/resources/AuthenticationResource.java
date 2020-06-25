package sumodashboard.resources;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
import sumodashboard.dao.AccountDAO;
import sumodashboard.model.Account;
import sumodashboard.model.Credentials;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@Path("/auth")
public class AuthenticationResource {
	@Context
	ContainerRequestContext requestContext;
	
	private boolean storeData = true;
	private static AccountDAO accountDAO = new AccountDAO();
	private Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
	private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final String API_TOKEN = "ZVXTyfmKXb7FxngTEAq2DHVmXZCxecJWTQLDsDnEce3dzhVK";
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogin(Credentials creds) {
		String username = creds.getUsername();
		
		String password = creds.getPassword();
		
		try {
			authenticate(username, password);	
			String token = createToken(username, 8000);
			NewCookie cookie = new NewCookie("session-id",token , "/", null, null, 300, false, false);
			System.out.println("Token created: " + token);
			return Response.ok("login successful").cookie(cookie).build();
		} catch(AuthenticationException e) {
			return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
		} catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogout() {
		try {
			NewCookie cookie = new NewCookie("session-id","" , "/", null, null, 300, false, false);
			return Response.ok("logout successful").cookie(cookie).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	private void authenticate(String username, String password) throws AuthenticationException, SQLException {
		String hashedPass = accountDAO.getHashedPassword(username);
		boolean passMatch = argon2.verify(hashedPass, password);
		if(!passMatch) throw new AuthenticationException("Password incorrect");
	}

	private static String createToken(String username, long ttlMillis) {
		try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create()
                    .withIssuedAt(issuedAt) // Issue date.
                    .withExpiresAt(expirationDate) // Expiration date.
                    .withClaim("username", username) // User id - here we can put anything we want, but for the example userId is appropriate.
                    .withIssuer("jwtauth") // Issuer of the token.
                    .sign(algorithm); // And the signing algorithm.
        } catch (JWTCreationException e) {
           	e.printStackTrace();
        }
        return null;
    }
	
	private static boolean validateToken(String token) {
		
		try {
			if(token != null) {
	            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
	            JWTVerifier verifier = JWT.require(algorithm)
	                    .withIssuer("jwtauth")
	                    .build(); //Reusable verifier instance
	            DecodedJWT jwt = verifier.verify(token);
	            //Get the userId from token claim.
	            String username = jwt.getClaim("username").asString();
	
	            return true;
	        }
	    } catch (JWTVerificationException e){
	    	System.out.println("validation failed");
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//Check if a rest request with a token is authorized
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
	
	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(Account acc) throws SQLException {
		//if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
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
			e.printStackTrace();
			return Response.status(Response.Status.CONFLICT).entity(respMessage).build();
		}
		if(!storeData) Response.ok("User created but not stored").build();
		return Response.ok("User created succesfully").build();
	}
	

}
