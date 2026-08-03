package jakbenimble.web;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;
import jakbenimble.spi.BootstrapExtension;

public class WebServer implements BootstrapExtension {

	Config config;
	UndertowJaxrsServer server;

	private static Logger logger = LoggerFactory.getLogger(WebServer.class);

	@Override
	public void configure(BeanManager bm) {
		this.config = ConfigProvider.getConfig();

		logger.trace("Creating RESTEasyDeployment...");
		ResteasyDeployment deployment = new ResteasyDeploymentImpl();

		for (Bean<?> bean : bm.getBeans(Object.class)) {
			logger.trace("Checking '" + bean.getBeanClass() + "'");
			AnnotatedType<?> type = bm.createAnnotatedType(bean.getBeanClass());
			if (type.isAnnotationPresent(Path.class)) {
				logger.debug("Found resource bean: " + bean.getBeanClass());
				deployment.getActualResourceClasses().add(bean.getBeanClass());
			}
			if (type.isAnnotationPresent(Provider.class)) {
				logger.debug("Found provider bean: " + bean.getBeanClass());
				deployment.getActualProviderClasses().add(bean.getBeanClass());
			}
		}

		logger.trace("Creating UnderTowJaxRsServer...");
		server = new UndertowJaxrsServer();
		int port = config.getValue("http.port", Integer.class);
		logger.debug("Using port: " + port);
		String host = config.getValue("http.host", String.class);
		logger.debug("Using host: " + host);

		logger.trace("Deploying ResteasyDeployment to UndertowJaxRsServer...");

		server.deploy(deployment);
		server.start(Undertow.builder().addListener(port, host));
	}

	@Override
	public void destroy() {
		if (server != null) {
			server.stop();
		}
	}

	@Override
	public String name() {
		return "JakBeNimble Web Server";
	}

}
