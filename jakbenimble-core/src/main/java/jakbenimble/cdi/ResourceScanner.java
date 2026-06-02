package jakbenimble.cdi;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;

public class ResourceScanner {
	private static final Logger logger = LoggerFactory.getLogger(ResourceScanner.class);

	ScanResult register(SeContainer container) {
		ScanResult results = ScanResult.getNewScanResult();
		BeanManager beanManager = container.getBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(Object.class);
		for (Bean<?> bean : beans) {
			logger.debug("Looking at bean: " + bean.getBeanClass().getName());
			Class<?> beanClass = bean.getBeanClass();
			if (beanClass.isAnnotationPresent(Path.class)) {
				logger.debug("Found resource: " + beanClass.getName());
				results.resources().add(beanClass);
			}
			if (beanClass.isAnnotationPresent(Provider.class)) {
				logger.debug("Found provider: " + beanClass.getName());
				results.providers().add(beanClass);
			}
		}
		return results;
	}
}
