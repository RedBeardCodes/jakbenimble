package jakbenimble;

import io.undertow.Undertow;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiServer {

	private Undertow server;

	public void start() {
		server = Undertow.builder().addHttpListener(8080, "localhost").build();
		server.start();
	}

	public void stop() {
		server.stop();
	}
}
