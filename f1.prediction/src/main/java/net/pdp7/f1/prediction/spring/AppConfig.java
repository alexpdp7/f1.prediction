package net.pdp7.f1.prediction.spring;

import javax.sql.DataSource;

import net.pdp7.f1.prediction.model.SchemaService;
import net.pdp7.f1.prediction.scraper.WikipediaScraper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class AppConfig {

	public @Bean DataSource dataSource() {
		return new SingleConnectionDataSource("jdbc:h2:mem:", true);
	}
	
	public @Bean SimpleJdbcTemplate jdbcTemplate() {
		return new SimpleJdbcTemplate(dataSource());
	}
	
	public @Bean SchemaService schemaService() {
		SchemaService schemaService = new SchemaService(jdbcTemplate());
		schemaService.createSchema();
		return schemaService;
	}
	
	public @Bean WikipediaScraper wikipediaScraper() {
		schemaService();
		return new WikipediaScraper(jdbcTemplate());
	}
	
}
