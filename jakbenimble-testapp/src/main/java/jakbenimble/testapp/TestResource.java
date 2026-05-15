package jakbenimble.testapp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("hello")
@ApplicationScoped
public class TestResource {
	@GET
	public String hello() {
		return "hello";
	}
}
