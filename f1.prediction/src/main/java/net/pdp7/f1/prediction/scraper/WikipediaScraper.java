package net.pdp7.f1.prediction.scraper;

import java.io.IOException;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

public class WikipediaScraper {
	
	protected final SimpleJdbcTemplate jdbcTemplate;

	public WikipediaScraper(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void scrape(int season) throws IOException {
		
		jdbcTemplate.update("merge into seasons(season) values (?)", season);
		
		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage("http://en.wikipedia.org/wiki/" + season + "_Formula_One_season");
		scrapeSeasonTeamDrivers(season, page);
	}

	protected void scrapeSeasonTeamDrivers(int season, HtmlPage page) {
		HtmlTable driversAndConstructorsTable = page.getFirstByXPath("//span[@id='Drivers_and_constructors' or @id='Teams_and_drivers']/../following-sibling::table[1]");
		for(int i=1; i<driversAndConstructorsTable.getRowCount(); i++) {
			String constructor = driversAndConstructorsTable.getCellAt(i, 1).asText();
			HtmlTableCell possibleDriverCell = driversAndConstructorsTable.getCellAt(i, 6);
			
			// rowspans (one row per driver, but constructor spans several rows) means sometimes we need to extract data in a different way...
			String driver = null;
			if(possibleDriverCell != null) {
				driver = possibleDriverCell.asText();
			}
			else {
				Object matching = driversAndConstructorsTable.getRow(i).getFirstByXPath("descendant::a[2]");
				
				// Sakon Yamamoto car change in 2010 means odd rowspanning!
				if(matching != null) {
					driver = ((HtmlAnchor) matching).asText();
				}
				else {
					continue;
				}
			}
			
			driver = driver.replaceAll("\\[.*\\]", "").trim();
			constructor = constructor.replaceAll("\\[.*\\]", "").trim();

			jdbcTemplate.update("merge into teams(team_name) values (?)", constructor);
			jdbcTemplate.update("merge into drivers(driver_name) values (?)", driver);
			
			jdbcTemplate.update("insert into season_team_drivers(season, team_name, driver_name) values (?,?,?)", season, constructor, driver);
		}
	}

}
