package jakbenimble;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakbenimble.spi.BootstrapExtension;

public final class JakBeNimble {
	private final SeContainerInitializer initializer;

	private SeContainer container;
	List<BootstrapExtension> extensions;

	private final Logger logger = LoggerFactory.getLogger(JakBeNimble.class);

	public static JakBeNimble start() {
		JakBeNimble jbn = new JakBeNimble(SeContainerInitializer.newInstance());
		jbn.doStart(true);
		return jbn;
	}

	JakBeNimble(SeContainerInitializer initializer) {
		this.initializer = initializer;
	}

	void doStart(boolean blockMainThread) {

		extensions = ServiceLoader.load(BootstrapExtension.class,
				Thread.currentThread().getContextClassLoader()).stream()
				.map(ServiceLoader.Provider::get).toList();

		container = initializer.initialize();
		for (BootstrapExtension ext : extensions) {
			BeanManager bm = container.getBeanManager();
			long start = System.currentTimeMillis();
			logger.info("Starting extension '{}'", ext.name());
			ext.configure(bm);
			long stop = System.currentTimeMillis();
			logger.info("Started extension '{}' in {} ms", ext.name(), stop - start);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for (BootstrapExtension ext : extensions) {
				long start = System.currentTimeMillis();
				logger.info("Stopping extension '{}'", ext.name());
				ext.destroy();
				long stop = System.currentTimeMillis();
				logger.info("Stopped extension '{}' in {} ms", ext.name(), stop - start);
			}
		}));
		if (blockMainThread) {
			CountDownLatch shutdown = new CountDownLatch(1);
			try {
				shutdown.await();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void stop() {
		if (container != null) {
			container.close();
			container = null;
		}
	}
}
