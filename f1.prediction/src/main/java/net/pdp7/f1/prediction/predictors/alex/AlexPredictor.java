package net.pdp7.f1.prediction.predictors.alex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.predictors.Predictor;

public class AlexPredictor implements Predictor {

	protected final RatingCalculator ratingCalculator;
	protected final SimpleJdbcTemplate jdbcTemplate;
	
	protected float driverPowerRatingDecayRate;

	public AlexPredictor(RatingCalculator ratingCalculator, SimpleJdbcTemplate jdbcTemplate, float driverPowerRatingDecayRate) {
		this.ratingCalculator = ratingCalculator;
		this.jdbcTemplate = jdbcTemplate;
		this.driverPowerRatingDecayRate = driverPowerRatingDecayRate;
	}
	
	public Prediction predict(int season, int round, String circuitName, Entrant[] entrants) {
		
		List<EntrantPower> entrantPowers = new ArrayList<EntrantPower>();
		
		for(Entrant entrant : entrants) {
			entrantPowers.add(new EntrantPower(entrant, calculatePowerRating(entrant, season, round, circuitName)));
		}
		
		Collections.sort(entrantPowers, EntrantPower.POWER_COMPARATOR);
		
		String[] topTen = new String[10];
		
		for(int i=0; i<10; i++) {
			topTen[i] = entrantPowers.get(i).entrant.driverName;
		}
		
		return new Prediction(entrantPowers.get(0).entrant.driverName, entrantPowers.get(0).entrant.driverName, topTen);
	}

	protected float calculatePowerRating(Entrant entrant, int season, int round, String circuitName) {
		
		float driverPowerRating = calculateDriverPowerRating(entrant, season, round, circuitName);
		
		return (float) driverPowerRating;
	}
	
	protected float calculateDriverPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   season, round " +
				"from     grand_prix_driver_results " +
				"where    driver_name = :driverName " +
				"and      (season < :season " +
				"          or season = :season and round < :round) " +
				"and      finish_position is not null " +
				"order by season desc, round desc", 
				MapUtils
					.<String,Object>build("driverName", entrant.driverName)
					.put("season", season)
					.put("round", round)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, driverPowerRatingDecayRate);
	}

	protected float calculatePowerRatingOnRounds(Entrant entrant, List<Map<String, Object>> previousSeasonRounds, float decayRate) {
		float ratings = 0.0f;
		float upperRatings = 0.0f;
		int distance = 1;
		
		for(Map<String, Object> previousSeasonRound : previousSeasonRounds) {
			int previousSeason = ((BigDecimal) previousSeasonRound.get("SEASON")).intValue();
			int previousRound = (Integer) previousSeasonRound.get("ROUND");
			
			float unadjustedPreviousRating = ratingCalculator.calculateDriverRating(previousSeason, previousRound, entrant.driverName);
			float upperRating = decayFactor(distance, decayRate);
			
			ratings += unadjustedPreviousRating * upperRating;
			upperRatings += upperRating;
			distance++;
		}
		
		return ratings/upperRatings;
	}
	
	protected float decayFactor(int ago, float scale) {
		return (float) Math.exp(-ago*Math.pow(scale/1.3,1.8));
	}
}
