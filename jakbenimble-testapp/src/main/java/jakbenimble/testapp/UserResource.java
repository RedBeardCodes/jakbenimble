package jakbenimble.testapp;

import java.time.Instant;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakbenimble.testapp.records.User;
import jakbenimble.testapp.records.UserResponse;
import query4j.Jdbc;
import query4j.exceptions.QueryException;

@ApplicationScoped
@Path("users")
public class UserResource {

	@Inject
	Jdbc jdbc;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() throws QueryException {
		String sql = "select * from users";
		List<UserResponse> users =jdbc.query(sql, UserResponse.MAPPER);
		return Response.ok().entity(users).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewUser(User user) throws QueryException {
		String sql = "insert into users (first_name, last_name, email, created_at, updated_at) values (?, ?, ?, ?, ?)";
		Integer id = jdbc.insert(sql, Integer.class, user.firstName(), user.lastName(), user.email(), Instant.now().getEpochSecond(), Instant.now().getEpochSecond());
		return Response.status(Status.CREATED).entity(id).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOneUserQueryParam(@PathParam("id") String id) throws QueryException {
		String sql = "select first_name, last_name, email from users where id = ?";
		User user = jdbc.queryOne(sql, User.MAPPER, id);
		return Response.ok().entity(user).build();
	}

	@PATCH
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOneUser(@PathParam("id") String id, User userToUpdate) throws QueryException {
		Integer userCount = jdbc.queryValue("select count(*) from users where id = ?", Integer.class, id);
		if (userCount != 1)
			return Response.status(Status.NOT_FOUND).build();
		String sql = "update users set first_name = ?, last_name = ?, email = ? where id = ?";
		int results = jdbc.update(sql, userToUpdate.firstName(), userToUpdate.lastName(), userToUpdate.email(), id);
		if (results > 0)
			return Response.status(Status.NO_CONTENT).build();
		else
			return Response.status(Status.BAD_REQUEST).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOneUser(@PathParam("id") String id) throws QueryException {
		Integer userCount = jdbc.queryValue("select count(*) from users where id = ?", Integer.class, id);
		if (userCount != 1)
			return Response.status(Status.NOT_FOUND).build();
		String sql = "delete from users where id = ?";
		int results = jdbc.update(sql, id);
		if (results > 0)
			return Response.status(Status.NO_CONTENT).build();
		else
			return Response.status(Status.BAD_REQUEST).build();
	}

}
