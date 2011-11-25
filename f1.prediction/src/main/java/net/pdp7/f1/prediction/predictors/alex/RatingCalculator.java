package net.pdp7.f1.prediction.predictors.alex;

import java.util.List;

import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class RatingCalculator {

	protected final SimpleJdbcTemplate jdbcTemplate;

	public RatingCalculator(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/** @return drivers who finished below driverName / total drivers who finished - 1, or 0 if driver did not finish */
	public float calculateDriverRating(int season, int round, String driverName) {
		if(!didDriverFinish(season, round, driverName)) {
			return 0;
		}
		
		int finishers = jdbcTemplate.queryForInt("select count(*) from grand_prix_driver_results where season = ? and round = ? and finish_position is not null", season, round);
		int finishPosition = jdbcTemplate.queryForInt("select finish_position from grand_prix_driver_results where season = ? and round = ? and driver_name = ?", season, round, driverName);
		
		return ((float) (finishers - finishPosition))/(finishers - 1);
	}

	protected boolean didDriverFinish(int season, int round, String driverName) {
		return jdbcTemplate.queryForInt("select count(*) from grand_prix_driver_results where season = ? and round = ? and finish_position is not null and driver_name = ?", season, round, driverName) == 1;
	}

	/** average driver rating of the team's drivers who finished, or 0 if no drivers finished */
	public float calculateTeamRating(int season, int round, String teamName) {
		List<String> teamDrivers = jdbcTemplate.query(
				"select grand_prix_driver_results.driver_name " +
				"from   grand_prix_driver_results " +
				"join   season_team_drivers on  grand_prix_driver_results.season = season_team_drivers.season " +
				"                           and grand_prix_driver_results.driver_name = season_team_drivers.driver_name " +
				"where  grand_prix_driver_results.season = ? " +
				"and    round = ? " +
				"and    team_name = ?", new SingleColumnRowMapper<String>(String.class), season, round, teamName);
		
		float totalRating = 0;
		int finishingDrivers = 0;
		
		for(String driverName : teamDrivers) {
			if(didDriverFinish(season, round, driverName)) {
				totalRating += calculateDriverRating(season, round, driverName);
				finishingDrivers++;
			}
		}
		
		return finishingDrivers == 0 ? 0 : totalRating/finishingDrivers;
	}
	
}
