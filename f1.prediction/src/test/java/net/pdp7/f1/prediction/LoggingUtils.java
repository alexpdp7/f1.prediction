package net.pdp7.f1.prediction;

import org.springframework.core.io.ClassPathResource;

import net.pdp7.commons.util.logging.SpringLogConfigurator;

public class LoggingUtils {

	protected LoggingUtils() {}
	
	public static void setupLogging() {
		new SpringLogConfigurator(new ClassPathResource("/net/pdp7/f1/prediction/logging.properties")).afterPropertiesSet();
	}
	
}
