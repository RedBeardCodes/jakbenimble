package jakbenimble.spi;

import jakarta.enterprise.inject.spi.BeanManager;

public interface BootstrapExtension {
	default String name() {
		return getClass().getSimpleName();
	}

	public void configure(BeanManager bm);

	public void destroy();
}
