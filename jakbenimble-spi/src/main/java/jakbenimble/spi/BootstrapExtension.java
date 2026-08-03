package jakbenimble.spi;

import jakarta.enterprise.inject.spi.BeanManager;

public interface BootstrapExtension {
	public void configure(BeanManager bm);

	public void destroy();
}
