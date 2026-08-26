package jakbenimble.testapp.exceptions;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import query4j.exceptions.NoResultException;
import query4j.exceptions.QueryException;

@Provider
@ApplicationScoped
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception) {
		String trackingId = UUID.randomUUID().toString();
		logger.error("Tracking ID: {}, Error message: {}", trackingId, exception.getMessage());
		switch (exception) {
			case NoResultException _ -> {
				ApiError error = new ApiError(trackingId, "No result found.");
				return Response.status(Status.NOT_FOUND).entity(error).type(MediaType.APPLICATION_JSON).build();
			}
			case QueryException qe -> {
				Throwable cause = qe.getCause();
				switch (cause) {
					case SQLiteException sqle -> {
						int code = sqle.getErrorCode();
						switch (code) {
							case 19 -> {
								String message = sqle.getMessage();
								if (message.startsWith("[SQLITE_CONSTRAINT_CHECK]")) {
									ApiError error = new ApiError(trackingId, "Constraint check failed.");
									return Response.status(Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON).build();
								} else if (message.startsWith("[SQLITE_CONSTRAINT_UNIQUE]")) {
									ApiError error = new ApiError(trackingId, "The resource you are trying to create already exists.");
									return Response.status(Status.CONFLICT).entity(error).type(MediaType.APPLICATION_JSON).build();
								} else {
									logger.info("SQLiteException tree");
									ApiError error = new ApiError(trackingId, "There was an internal server error.");
									return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
								}
							}
							default -> {
								logger.info("{}", sqle.getErrorCode());
								ApiError error = new ApiError(trackingId, "There was an internal server error.");
								return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
							}
						}
					}
					default -> { logger.info(exception.getMessage()); }
				}
			}
			default -> {
				logger.info(exception.getMessage());
				ApiError error = new ApiError(trackingId, "There was an internal server error.");
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	public record ApiError(String tackingId, String message) {}
}
