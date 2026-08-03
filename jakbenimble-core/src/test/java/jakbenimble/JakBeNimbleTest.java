package jakbenimble;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.mockito.Mockito.*;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

@Execution(ExecutionMode.SAME_THREAD)
public class JakBeNimbleTest {
	@Test
	public void shouldInitializeContainer() {
		var initializer = mock(SeContainerInitializer.class);
		var container = mock(SeContainer.class);

		when(initializer.initialize()).thenReturn(container);

		var app = new JakBeNimble(initializer);
		app.doStart(false);

		verify(initializer).initialize();
	}

	@Test
	public void shouldCloseContainerOnStop() {
		var initializer = mock(SeContainerInitializer.class);
		var container = mock(SeContainer.class);

		when(initializer.initialize()).thenReturn(container);

		var app = new JakBeNimble(initializer);
		app.doStart(false);

		verify(initializer).initialize();
		app.stop();
		verify(container).close();
	}
}
