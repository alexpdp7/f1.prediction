package net.pdp7.f1.prediction.scraper;

import net.pdp7.f1.prediction.spring.AppConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import junit.framework.TestCase;

public class WikipediaScraperTest extends TestCase {

	public void test() throws Exception {
		
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		
		WikipediaScraper wikipediaScraper = applicationContext.getBean("wikipediaScraper", WikipediaScraper.class);
		
		for(int i=2005; i<=2011; i++) {
			wikipediaScraper.scrape(i);
		}
		
		SimpleJdbcTemplate jdbcTemplate = applicationContext.getBean("jdbcTemplate", SimpleJdbcTemplate.class);
		
		System.out.println(jdbcTemplate.queryForList("select * from seasons order by season"));
		System.out.println(jdbcTemplate.queryForList("select * from teams order by team_name"));
		System.out.println(jdbcTemplate.queryForList("select * from drivers order by driver_name"));
		System.out.println(jdbcTemplate.queryForList("select * from season_team_drivers order by season, team_name, driver_name"));
		System.out.println(jdbcTemplate.queryForList("select * from circuits order by circuit_name"));
		System.out.println(jdbcTemplate.queryForList("select * from calendar order by season, round"));
	}
	
}
