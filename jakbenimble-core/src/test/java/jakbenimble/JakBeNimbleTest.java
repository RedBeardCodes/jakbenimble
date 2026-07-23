package jakbenimble;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.mockito.Mockito.*;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.Extension;

@Execution(ExecutionMode.SAME_THREAD)
public class JakBeNimbleTest {
	@Test
	public void shouldInitializeContainer() {
		var config = new JakBeNimbleConfig("0.0.0.0", 0, false);
		var initializer = mock(SeContainerInitializer.class);
		var container = mock(SeContainer.class);

		when(initializer.addExtensions(any(Extension.class))).thenReturn(initializer);
		when(initializer.initialize()).thenReturn(container);

		var app = new JakBeNimble(config, initializer);
		app.doStart();

		verify(initializer).initialize();
	}

	@Test
	public void shouldRegisterDiscoveredResources() {
		var config = new JakBeNimbleConfig("0.0.0.0", 0, false);
		var initializer = mock(SeContainerInitializer.class);
		var container = mock(SeContainer.class);

		when(initializer.addExtensions(any(Extension.class))).thenReturn(initializer);
		when(initializer.initialize()).thenReturn(container);

		var app = new JakBeNimble(config, initializer);
		app.doStart();

		verify(initializer).initialize();
	}

	@Test
	public void shouldCloseContainerOnStop() {
		var config = new JakBeNimbleConfig("0.0.0.0", 0, false);
		var initializer = mock(SeContainerInitializer.class);
		var container = mock(SeContainer.class);

		when(initializer.addExtensions(any(Extension.class))).thenReturn(initializer);
		when(initializer.initialize()).thenReturn(container);

		var app = new JakBeNimble(config, initializer);
		app.doStart();

		verify(initializer).initialize();
		app.stop();
		verify(container).close();
	}
}
