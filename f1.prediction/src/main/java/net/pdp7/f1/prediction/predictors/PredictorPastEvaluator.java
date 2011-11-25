package net.pdp7.f1.prediction.predictors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.pdp7.f1.prediction.predictors.Predictor.Entrant;
import net.pdp7.f1.prediction.predictors.Predictor.Prediction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class PredictorPastEvaluator {

	protected final SimpleJdbcTemplate jdbcTemplate;
	protected final PredictionScorer predictionScorer;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public PredictorPastEvaluator(SimpleJdbcTemplate jdbcTemplate, PredictionScorer predictionScorer) {
		this.jdbcTemplate = jdbcTemplate;
		this.predictionScorer = predictionScorer;
	}
	
	public int evaluate(final Predictor predictor) {
		List<Integer> scores = jdbcTemplate.query(
				"select distinct calendar.season, calendar.round, calendar.circuit_name " +
				"from   grand_prix_driver_results " +
				"join   calendar on grand_prix_driver_results.season = calendar.season and grand_prix_driver_results.round = calendar.round " +
				"order by calendar.season, calendar.round", new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int season = rs.getInt("season");
				int round = rs.getInt("round");
				
				Entrant[] entrants = jdbcTemplate.query(
						"select team_name, grand_prix_driver_results.driver_name " +
						"from   grand_prix_driver_results " +
						"join   season_team_drivers on grand_prix_driver_results.season = season_team_drivers.season and grand_prix_driver_results.driver_name = season_team_drivers.driver_name " +
						"where  grand_prix_driver_results.season = ? " +
						"and    grand_prix_driver_results.round = ?", 
						new RowMapper<Entrant>() {
							public Entrant mapRow(ResultSet rs, int rowNum) throws SQLException {
								return new Entrant(rs.getString("team_name"), rs.getString("driver_name"));
							}
						}, 
						season, round).toArray(new Entrant[0]);
				Prediction prediction = predictor.predict(season, round, rs.getString("circuit_name"), entrants);
				int score = predictionScorer.score(prediction, season, round);
				logger.debug("season {} round {} prediction {} score {}", new Object[] { season, round, prediction, score});
				return score;
			}
		});

		int evaluation = 0;

		for(int score : scores) {
			evaluation += score;
		}
		
		return evaluation;
	}
}
