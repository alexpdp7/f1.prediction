package net.pdp7.f1.prediction.spring;

import net.pdp7.commons.util.logging.SpringLogConfigurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class LogConfig {
	
	public @Bean SpringLogConfigurator springLogConfigurator() {
		SpringLogConfigurator springLogConfigurator = new SpringLogConfigurator(logConfigurationResource());
		springLogConfigurator.afterPropertiesSet();
		return springLogConfigurator;
	}

	public @Bean Resource logConfigurationResource() {
		return new ClassPathResource("/net/pdp7/f1/prediction/logging.properties");
	}

}
