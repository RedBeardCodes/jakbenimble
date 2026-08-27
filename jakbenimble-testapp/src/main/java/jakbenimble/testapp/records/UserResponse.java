package jakbenimble.testapp.records;

import java.time.Instant;

import query4j.RowMapper;
import query4j.domain.Activable;
import query4j.domain.Auditable;
import query4j.domain.Sequenceable;

public record UserResponse(Long id, String firstName, String lastName, String email, Instant createdAt, Instant updatedAt, boolean isActive) implements Sequenceable, Auditable, Activable {
	public static final RowMapper<UserResponse> MAPPER = rs -> {
		return new UserResponse(
				rs.getLong("id"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email"),
				Instant.ofEpochSecond(rs.getLong("created_at")),
				Instant.ofEpochSecond(rs.getLong("updated_at")),
				rs.getInt("is_active") == 1
				);
	};
}
