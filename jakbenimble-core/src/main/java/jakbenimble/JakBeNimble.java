package jakbenimble;

import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public final class JakBeNimble {

	private SeContainer container;
	private UndertowJaxrsServer server;

	private static final Logger logger = LoggerFactory.getLogger(JakBeNimble.class);

	public static JakBeNimble start(JakBeNimbleConfig config) {
		return new JakBeNimble().doStart(config);
	}

	private JakBeNimble doStart(JakBeNimbleConfig config) {
		container = SeContainerInitializer.newInstance().initialize();

		logger.debug("Setting up undertow and resteasy...");
		server = new UndertowJaxrsServer();
		server.start(Undertow.builder().addHttpListener(config.port(), config.host()));
		ResteasyDeployment deployment = new ResteasyDeploymentImpl();
		ResourceScanner scanner = new ResourceScanner();
		ScanResult results = scanner.register(container);
		deployment.getActualResourceClasses().addAll(results.resources());
		deployment.getActualProviderClasses().addAll(results.providers());
		server.deploy(deployment);

		return this;
	}

	public void stop() {
		server.stop();
		container.close();
	}
}
