package net.pdp7.f1.prediction.spring;

import net.pdp7.f1.prediction.model.SchemaService;
import net.pdp7.f1.prediction.predictors.PredictionScorer;
import net.pdp7.f1.prediction.predictors.PredictorPastEvaluator;
import net.pdp7.f1.prediction.scraper.CircuitNameNormalizer;
import net.pdp7.f1.prediction.scraper.WikipediaScraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

@Configuration
public class F1PredictionConfig {

	@Autowired protected DataSourceConfig dataSourceConfig;
	
	public @Bean SimpleJdbcTemplate jdbcTemplate() {
		return new SimpleJdbcTemplate(dataSourceConfig.dataSource());
	}
	
	public @Bean SchemaService schemaService() {
		return new SchemaService(jdbcTemplate());
	}
	
	public @Bean WikipediaScraper wikipediaScraper() {
		return new WikipediaScraper(jdbcTemplate(), circuitNameNormalizer());
	}
	
	public @Bean CircuitNameNormalizer circuitNameNormalizer() {
		return new CircuitNameNormalizer(synonymsFile());
	}

	public @Bean Resource synonymsFile() {
		return new ClassPathResource("/net/pdp7/f1/prediction/scraper/circuit_synonyms.txt");
	}

	public @Bean PredictorPastEvaluator predictorPastEvaluator() {
		return new PredictorPastEvaluator(jdbcTemplate(), predictionScorer());
	}

	public @Bean PredictionScorer predictionScorer() {
		return new PredictionScorer(jdbcTemplate());
	}
	
}
