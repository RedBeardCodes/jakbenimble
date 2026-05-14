package jakbenimble;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Application {
	@Inject
	ApiServer apiServer;

	public void start() {
		apiServer.start();
	}

	public void stop() {
		apiServer.stop();
	}
}
