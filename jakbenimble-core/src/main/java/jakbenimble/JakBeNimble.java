package jakbenimble;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public final class JakBeNimble {

	private SeContainer container;

	public static JakBeNimble start() {
		return new JakBeNimble().doStart();
	}

	private JakBeNimble doStart() {
		container = SeContainerInitializer.newInstance().initialize();
		container.select(Application.class).get().start();
		return this;
	}

	public void stop() {
		container.close();
	}
}
