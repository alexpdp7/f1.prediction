package net.pdp7.commons.spring.context.annotation;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextUtils {

	public static AnnotationConfigApplicationContext createConfiguredAnnotationConfigApplicationContext(Properties properties, Class<?>... configs) {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(properties);
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.addBeanFactoryPostProcessor(propertyPlaceholderConfigurer);
		applicationContext.register(configs);
		applicationContext.refresh();
		return applicationContext;
	}

}
