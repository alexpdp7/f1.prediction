package net.pdp7.f1.prediction.predictors.alex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.predictors.Predictor;

public class AlexPredictor implements Predictor {

	protected final RatingCalculator ratingCalculator;
	protected final SimpleJdbcTemplate jdbcTemplate;
	protected final AlexPredictorParams alexPredictorParams;
	

	public AlexPredictor(RatingCalculator ratingCalculator, SimpleJdbcTemplate jdbcTemplate, AlexPredictorParams alexPredictorParams) {
		this.ratingCalculator = ratingCalculator;
		this.jdbcTemplate = jdbcTemplate;
		this.alexPredictorParams = alexPredictorParams;
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

	/** @return -1 if driver hasn't raced before */
	protected float calculatePowerRating(Entrant entrant, int season, int round, String circuitName) {
		
		float driverPowerRating = calculateDriverPowerRating(entrant, season, round, circuitName);
		float driverCircuitPowerRating = calculateDriverCircuitPowerRating(entrant, season, round, circuitName);
		
		float result = 0;
		float upperBound = 0;
		
		if(driverPowerRating >= 0) {
			result += driverPowerRating * alexPredictorParams.driverPowerWeight;
			upperBound += alexPredictorParams.driverPowerWeight;
		}
		
		if(driverCircuitPowerRating >= 0) {
			result += driverCircuitPowerRating * alexPredictorParams.driverCircuitPowerWeight;
			upperBound += alexPredictorParams.driverCircuitPowerWeight;
		}

		return upperBound >= 0.001 ? result / upperBound : 0;
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
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.driverPowerRatingDecayRate);
	}

	/** @return -1 if driver hasn't raced in this circuit */
	protected float calculateDriverCircuitPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   grand_prix_driver_results.season, grand_prix_driver_results.round " +
				"from     grand_prix_driver_results " +
				"join     calendar on  grand_prix_driver_results.season = calendar.season " +
				"                  and grand_prix_driver_results.round = calendar.round " +
				"where    driver_name = :driverName " +
				"and      (grand_prix_driver_results.season < :season " +
				"          or grand_prix_driver_results.season = :season and grand_prix_driver_results.round < :round) " +
				"and      circuit_name = :circuitName " +
				"and      finish_position is not null " +
				"order by grand_prix_driver_results.season desc, grand_prix_driver_results.round desc", 
				MapUtils
					.<String,Object>build("driverName", entrant.driverName)
					.put("season", season)
					.put("round", round)
					.put("circuitName", circuitName)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.driverCircuitPowerRatingDecayRate);
	}

	/** @return -1 if previousSeasonRounds is empty */
	protected float calculatePowerRatingOnRounds(Entrant entrant, List<Map<String, Object>> previousSeasonRounds, float decayRate) {
		
		if(previousSeasonRounds.isEmpty()) {
			return -1;
		}
		
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
	
	public static class AlexPredictorParams {

		public final float driverPowerRatingDecayRate;
		public final float driverCircuitPowerRatingDecayRate;
		public final float driverPowerWeight;
		public final float driverCircuitPowerWeight;

		public AlexPredictorParams(float driverPowerRatingDecayRate, float driverCircuitPowerRatingDecayRate, float driverPowerWeight, float driverCircuitPowerWeight) {
			this.driverPowerRatingDecayRate = driverPowerRatingDecayRate;
			this.driverCircuitPowerRatingDecayRate = driverCircuitPowerRatingDecayRate;
			this.driverPowerWeight = driverPowerWeight;
			this.driverCircuitPowerWeight = driverCircuitPowerWeight;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
		
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		
		public static AlexPredictorParams randomParams() {
			return new AlexPredictorParams(
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random());
		}
	}
}
