package jakbenimble;

import java.util.Set;

import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;

public final class JakBeNimble {

	private SeContainer container;
	private UndertowJaxrsServer server;

	private static final Logger logger = LoggerFactory.getLogger(JakBeNimble.class);

	public static JakBeNimble start() {
		return new JakBeNimble().doStart();
	}

	private JakBeNimble doStart() {
		container = SeContainerInitializer.newInstance().initialize();

		logger.debug("Setting up undertow and resteasy...");
		server = new UndertowJaxrsServer();
		server.start(Undertow.builder().addHttpListener(8080, "localhost"));
		ResteasyDeployment deployment = new ResteasyDeploymentImpl();
		registerResources(deployment);
		server.deploy(deployment);

		return this;
	}

	private void registerResources(ResteasyDeployment deployment) {
		BeanManager beanManager = container.getBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(Object.class);
		for (Bean<?> bean : beans) {
			logger.debug("Looking at bean: " + bean.getBeanClass().getName());
			Class<?> beanClass = bean.getBeanClass();
			if (beanClass.isAnnotationPresent(Path.class)) {
				logger.debug("Found resource: " + beanClass.getName());
				deployment.getActualResourceClasses().add(beanClass);
			}
			if (beanClass.isAnnotationPresent(Provider.class)) {
				logger.debug("Found provider: " + beanClass.getName());
				deployment.getActualProviderClasses().add(beanClass);
			}
		}
	}

	public void stop() {
		server.stop();
		container.close();
	}
}
