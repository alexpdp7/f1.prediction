package net.pdp7.f1.prediction.scraper;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class WikipediaScraper {
	
	public void scrape(int season) throws IOException {
		WebClient webClient = new WebClient();
		Page page = webClient.getPage("http://en.wikipedia.org/wiki/" + season + "_Formula_One_season");
		
	}

}
