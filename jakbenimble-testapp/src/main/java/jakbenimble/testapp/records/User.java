package jakbenimble.testapp.records;

import query4j.RowMapper;

public record User(String firstName, String lastName, String email) {
	public static final RowMapper<User> MAPPER = rs -> {
		return new User(
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email")
				);
	};
}
