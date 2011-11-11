package net.pdp7.f1.prediction.scraper;

import junit.framework.TestCase;

public class WikipediaScraperTest extends TestCase {

	public void test() throws Exception {
		WikipediaScraper wikipediaScraper = new WikipediaScraper();
		
		for(int i=2005; i<=2011; i++) {
			wikipediaScraper.scrape(i);
		}
	}
	
}
