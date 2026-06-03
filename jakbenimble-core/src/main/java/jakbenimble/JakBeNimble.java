package jakbenimble;

import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakbenimble.cdi.RestDiscoveryExtension;

public final class JakBeNimble {
	private final JakBeNimbleConfig config;
	private final SeContainerInitializer initializer;

	private SeContainer container;
	private UndertowJaxrsServer server;

	private static final Logger logger = LoggerFactory.getLogger(JakBeNimble.class);

	public static JakBeNimble start(JakBeNimbleConfig config) {
		JakBeNimble jbn = new JakBeNimble(config, SeContainerInitializer.newInstance());
		jbn.doStart();
		return jbn;
	}

	JakBeNimble(JakBeNimbleConfig config, SeContainerInitializer initializer) {
		this.config = config;
		this.initializer = initializer;
	}

	void doStart() {
		try {
			logger.trace("Creating RestDiscoveryExtension...");
			RestDiscoveryExtension ext = new RestDiscoveryExtension();
			container = initializer.addExtensions(ext).initialize();

			logger.trace("Creating ResteasyDeployment...");
			ResteasyDeployment deployment = new ResteasyDeploymentImpl();
			deployment.getActualResourceClasses().addAll(ext.getResources());
			deployment.getActualProviderClasses().addAll(ext.getProviders());

			logger.trace("Creating UnderTowJaxRsServer...");
			server = new UndertowJaxrsServer();
			server.start(Undertow.builder().addHttpListener(config.port(), config.host()));

			logger.trace("Deploying ResteasyDeployment to UndertowJaxRsServer...");
			server.deploy(deployment);
		} catch (Exception e) {
			throw new RuntimeException("Failed to start JakBeNimble", e);
		}
	}

	public void stop() {
		if (server != null) {
			server.stop();
			server = null;
		}
		if (container != null) {
			container.close();
			container = null;
		}
	}
}
