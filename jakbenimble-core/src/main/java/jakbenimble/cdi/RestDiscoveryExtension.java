package jakbenimble.cdi;

import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;

public class RestDiscoveryExtension implements Extension {
	private final Set<Class<?>> resources = new HashSet<>();
	private final Set<Class<?>> providers = new HashSet<>();

	void process(@Observes ProcessAnnotatedType<?> pat) {
		Class<?> clazz = pat.getAnnotatedType().getJavaClass();

		if (clazz.isAnnotationPresent(Path.class))
			resources.add(clazz);
		if (clazz.isAnnotationPresent(Provider.class))
			providers.add(clazz);
	}

	public Set<Class<?>> getResources() {
		return resources;
	}

	public Set<Class<?>> getProviders() {
		return providers;
	}
}
