package net.pdp7.f1.prediction.scraper;


import junit.framework.TestCase;
import net.pdp7.commons.spring.context.annotation.AnnotationConfigApplicationContextUtils;
import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.model.SchemaService;
import net.pdp7.f1.prediction.spring.DataSourceConfig;
import net.pdp7.f1.prediction.spring.F1PredictionConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WikipediaScraperTest extends TestCase {

	public void test() throws Exception {
		
		AnnotationConfigApplicationContext applicationContext = AnnotationConfigApplicationContextUtils.createConfiguredAnnotationConfigApplicationContext(
				MapUtils.createPropertiesFromMap(MapUtils.build("jdbc.url", "jdbc:h2:mem:").map), 
				F1PredictionConfig.class, DataSourceConfig.JdbcUrlDataSourceConfig.class);
		
		applicationContext.getBean("schemaService", SchemaService.class).createSchema();
		WikipediaScraper wikipediaScraper = applicationContext.getBean("wikipediaScraper", WikipediaScraper.class);
		
		for(int i=2005; i<=2011; i++) {
			wikipediaScraper.scrape(i);
		}
	}
}
