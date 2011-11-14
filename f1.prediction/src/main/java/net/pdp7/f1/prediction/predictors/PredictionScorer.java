package net.pdp7.f1.prediction.predictors;

import java.util.List;

import net.pdp7.f1.prediction.predictors.Predictor.Prediction;

import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class PredictionScorer {

	protected final SimpleJdbcTemplate jdbcTemplate;
	
	public static int POINTS_FASTEST_LAP = 2;
	public static int POINTS_POLE_POSITION = 2;
	public static int POINTS_CORRECT_WINNER = 1;
	public static int POINTS_CORRECT_NAME_TOP_3 = 1;
	public static int POINTS_CORRECT_NAME_TOP_10 = 1;
	public static final int POINTS_EXACT_POSITION_TOP_10 = 1;
	
	public PredictionScorer(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int score(Predictor.Prediction prediction, int season, int round) {
		int score = 0;
		
		score += 
				prediction.pole == 
				jdbcTemplate.queryForObject("select driver_name from grand_prix_driver_results where season = ? and round = ? and pole", String.class, season, round)
				? POINTS_POLE_POSITION : 0;

		score += 
				prediction.fastestLap == 
				jdbcTemplate.queryForObject("select driver_name from grand_prix_driver_results where season = ? and round = ? and fastest_lap", String.class, season, round)
				? POINTS_FASTEST_LAP : 0;

		score += pointsForOverallCoincidences(prediction, season, round, 1, 1) * POINTS_CORRECT_WINNER;
		score += pointsForOverallCoincidences(prediction, season, round, 1, 3) * POINTS_CORRECT_NAME_TOP_3;
		score += pointsForOverallCoincidences(prediction, season, round, 1, 10) * POINTS_CORRECT_NAME_TOP_10;
		score += pointsForExactCoincidences(prediction, season, round, 1, 10) * POINTS_EXACT_POSITION_TOP_10;
		
		return score;
	}

	private int pointsForExactCoincidences(Prediction prediction, int season, int round, int from, int to) {
		int coincidences = 0;
		
		List<String> finishPositions = jdbcTemplate.query("select driver_name from grand_prix_driver_results where season = ? and round = ? and finish_position between ? and ? order by finish_position", new SingleColumnRowMapper<String>(String.class), season, round, from, to);
		
		for(int i=from; i<= from + (finishPositions.size() -1); i++) {
			coincidences += prediction.topTen[i-from].equals(finishPositions.get(i-from)) ? 1 : 0;
		}
		
		return coincidences;
	}

	protected int pointsForOverallCoincidences(Prediction prediction, int season, int round, int from, int to) {
		int coincidences = 0;
		
		for(int i=from; i<=to; i++) {
			coincidences += jdbcTemplate.queryForInt(
					"select count(*) " +
					"from grand_prix_driver_results " +
					"where season = ? " +
					"and round = ? " +
					"and driver_name = ? " +
					"and finish_position between ? and ?", season, round, prediction.topTen[i-1], from, to);
		}
		
		return coincidences;
	}
	
}
