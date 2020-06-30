package sumodashboard.resources;

import java.util.ArrayList;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTagsResource {
	private static final String AUTHHEADER = "Bearer ZVXTyfmKXb7FxngTEAq2DHVmXZCxecJWTQLDsDnEce3dzhVK";
	UriInfo uriInfo;
	Request request;
	ContainerRequestContext requestContext;
	
	@BeforeAll
	public void setUp() {
		//Pretend to send request with ?vehicle=v5 and ?lane=e5 in URI
		uriInfo = Mockito.mock(UriInfo.class);
		request = Mockito.mock(Request.class);
		requestContext = Mockito.mock(ContainerRequestContext.class);
		Mockito.when(requestContext.getHeaderString("Authorization")).thenReturn(AUTHHEADER);
	}
	
	@Test
	public void testGetTags() {
		Response getTagsResponse = new TagsResource(uriInfo, request, requestContext).getTags();
		Assertions.assertEquals(200, getTagsResponse.getStatus());
		
		Object body = getTagsResponse.getEntity();
		Assertions.assertNotNull(body);
		Assertions.assertTrue(body instanceof ArrayList<?>);
		
		@SuppressWarnings("unchecked")
		ArrayList<String> tags = (ArrayList<String>) body;
		Assertions.assertNotNull(tags.get(0));
	}
}