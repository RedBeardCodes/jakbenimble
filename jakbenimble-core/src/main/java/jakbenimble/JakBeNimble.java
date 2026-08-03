package jakbenimble;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakbenimble.spi.BootstrapExtension;

public final class JakBeNimble {
	private final SeContainerInitializer initializer;

	private SeContainer container;
	List<BootstrapExtension> extensions;

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
			ext.configure(bm);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for (BootstrapExtension ext : extensions) {
				ext.destroy();
				initializer.addBeanClasses(ext.getClass());
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
