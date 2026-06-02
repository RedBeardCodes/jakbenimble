package jakbenimble;

import java.util.Objects;

public record JakBeNimbleConfig(String host, int port, boolean devMode) {
	public JakBeNimbleConfig {
		Objects.nonNull(host);

		if (port < 1 || port > 65535)
			throw new IllegalArgumentException("Invalid port: " + port);
	}

	public static JakBeNimbleConfig defaults() {
		return new JakBeNimbleConfig("0.0.0.0", 8080, false);
	}
}
