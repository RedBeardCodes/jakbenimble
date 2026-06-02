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

		RestDiscoveryExtension ext = container.getBeanManager().getExtension(RestDiscoveryExtension.class);
		deployment.getActualResourceClasses().addAll(ext.getResources());
		deployment.getActualProviderClasses().addAll(ext.getProviders());
		server.deploy(deployment);

		return this;
	}

	public void stop() {
		server.stop();
		container.close();
	}
}
