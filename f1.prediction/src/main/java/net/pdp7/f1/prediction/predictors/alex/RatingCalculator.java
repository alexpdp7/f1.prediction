package net.pdp7.f1.prediction.predictors.alex;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class RatingCalculator {

	protected final SimpleJdbcTemplate jdbcTemplate;

	public RatingCalculator(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public float calculateDriverRating(int season, int round, String driverName) {
		if(jdbcTemplate.queryForInt("select count(*) from grand_prix_driver_results where season = ? and round = ? and finish_position is not null and driver_name = ?", season, round, driverName) == 0) {
			return 0;
		}
		
		int finishers = jdbcTemplate.queryForInt("select count(*) from grand_prix_driver_results where season = ? and round = ? and finish_position is not null", season, round);
		int finishPosition = jdbcTemplate.queryForInt("select finish_position from grand_prix_driver_results where season = ? and round = ? and driver_name = ?", season, round, driverName);
		
		return ((float) (finishers - finishPosition))/(finishers - 1);
	}
	
}
