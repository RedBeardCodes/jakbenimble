package jakbenimble.cdi;

import java.util.HashSet;
import java.util.Set;

public record ScanResult(
		Set<Class<?>> resources,
		Set<Class<?>> providers) {

	public static ScanResult getNewScanResult() {
		Set<Class<?>> resources = new HashSet<>() {
		};
		Set<Class<?>> providers = new HashSet<>() {
		};
		return new ScanResult(resources, providers);
	}
}
